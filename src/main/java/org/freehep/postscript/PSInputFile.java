// Copyright 2001-2004, FreeHEP.
package org.freehep.postscript;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipException;

/**
 * Objects for PostScript Processor, as defined in 3.3 Data Types and Objects
 *
 * @author Mark Donszelmann
 * @version $Id: src/main/java/org/freehep/postscript/PSInputFile.java 17245790f2a9 2006/09/12 21:44:14 duns $
 */
public class PSInputFile extends PSFile implements PSTokenizable, PSDataSource {
    protected InputStream in = null;
    protected Scanner scanner = null;
    protected DSC dsc = null;
    
    protected PSInputFile(String n, boolean f, InputStream i, Scanner s, DSC d) {
        super(n, f);
        in = i;
        scanner = s;
        dsc = d;
    }
    
    public PSInputFile(InputStream input, DSC dsc) throws IOException {
        super("pipe", true);
        init(input, dsc);
    }
    
    public PSInputFile(String filename) throws FileNotFoundException, IOException {
        this(filename, new DSC());
    }
    
    public PSInputFile(String filename, DSC dsc) throws FileNotFoundException, IOException {
        super(filename, false);
        
        InputStream input;
        try {
            URL url = new URL(filename);
            input = url.openStream();
            // FIXME maybe we should modify name here to something shorter...
        } catch (MalformedURLException e) {
            input = new FileInputStream(filename);
        } catch (ZipException e) {
            throw new FileNotFoundException("Archive cannot be found: "+filename);
        } 
        
        if (filename.toLowerCase().endsWith(".gz")) {
            input = new GZIPInputStream(input);
        }
        init(input, dsc);
    }
        
    private void init(InputStream input, DSC dsc) throws IOException {
        if (!filter) input = new BufferedInputStream(input);
        in = new PushbackInputStream(input);
        access = READ_ONLY;
        this.dsc = dsc;
    }
    
    public InputStream getInputStream() {
        return in;
    }
    
    public DSC getDSC() {
        return dsc;
    }
    
    private void getScanner() throws IOException {
        if (scanner == null) {
            if (in == null) {
                throw new IOException();
            }
            scanner = new Scanner((PushbackInputStream)in, dsc);
        }
    }

    public PSObject token(boolean packingMode, NameLookup lookup) throws IOException, SyntaxException, NameNotFoundException {
        getScanner();
        return scanner.nextToken(packingMode, lookup);
    }
    
    public boolean execute(OperandStack os) {
	if (in == null) return true;
        try {
            getScanner();
        } catch (IOException e) {
            error(os, new IOError());
            return true;
        }
        return Dispatcher.dispatch(os, this);       
    }
    
    public void close() throws IOException {
        if (in != null) {
            if (!filter) {
                in.close();
            }
	    in = null;
	    scanner = null;
        }
    }
    
    public int read() throws IOException {
        return (in != null) ? in.read() : -1;
    }
    
    private BufferedReader reader;
    public String readLine() throws IOException {
        try {
            if (in == null) {
                return null;
            }
            
            if (reader == null) {
                reader = new BufferedReader(new InputStreamReader(in));
            }
            return reader.readLine();
            
        } catch (ClassCastException e) {
            throw new IOException("Cannot readLine");
        }
    }
                
    public int available() throws IOException {
        return (in != null) ? in.available() : -1;
    }        
    
    public void flush() throws IOException {
        if (in != null) {
            int b;
            do {
                b = in.read();
            } while (b != -1);

            if (filter) {
                close();
            }
        }
    }

    public boolean markSupported() {
        return (in != null) ? in.markSupported() : false;
    }
    
    public void mark(int readLimit) {
        if (in != null) {
            in.mark(readLimit);
        }
    }

    public void reset() throws IOException {
        if (in != null) {
            in.reset();
        }
    }
    
    public boolean isValid() {
        return (in != null);
    }
    
    public int hashCode() {
        return in.hashCode();
    }

    public boolean equals(Object o) {
        if (o instanceof PSInputFile) {
            return (in == ((PSInputFile)o).in);
        }
        return false;
    }

    public Object clone() {
        return new PSInputFile(filename, filter, in, scanner, dsc);
    }

    public PSObject copy() {
        if (filter) throw new RuntimeException("Filters cannot be copied");
        
        try {
            return new PSInputFile(filename, dsc);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Cannot find file while copying: "+filename);
        } catch (IOException e) {
            throw new RuntimeException("IOException for file while copying: "+filename);
        }
    }
}

