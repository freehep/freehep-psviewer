// Copyright 2001-2010, FreeHEP.
package org.freehep.postscript.operators;

import java.io.IOException;
import java.util.logging.Level;

import org.freehep.postscript.errors.IOError;
import org.freehep.postscript.errors.TypeCheck;
import org.freehep.postscript.errors.Undefined;
import org.freehep.postscript.stacks.OperandStack;
import org.freehep.postscript.types.PSBoolean;
import org.freehep.postscript.types.PSDataSource;
import org.freehep.postscript.types.PSDevice;
import org.freehep.postscript.types.PSDictionary;
import org.freehep.postscript.types.PSGState;
import org.freehep.postscript.types.PSInteger;
import org.freehep.postscript.types.PSNumber;
import org.freehep.postscript.types.PSObject;
import org.freehep.postscript.types.PSPackedArray;
import org.freehep.postscript.types.PSString;
import org.freehep.vectorgraphics.Image;
import org.freehep.vectorgraphics.Path;
import org.freehep.vectorgraphics.Rectangle;

/**
 * Painting Operators for PostScript Processor
 * 
 * @author Mark Donszelmann
 */
public abstract class PaintingOperator extends AbstractOperator {

	public static void register(PSDictionary dict) {
		AbstractOperator.register(dict, new Class<?>[] { ErasePage.class,
				Stroke.class, Fill.class, EOFill.class, RectStroke.class,
				RectFill.class, UStroke.class, UFill.class, UEOFill.class,
				ShFill.class, PaintImage.class, ColorImage.class,
				ImageMask.class });
	}
}

class ErasePage extends PaintingOperator {

	@Override
	public boolean execute(OperandStack os) {
		os.gstate().erasePage();
		return true;
	}
}

class Stroke extends PaintingOperator {

	@Override
	public boolean execute(OperandStack os) {
		// FREEHEP-156: add special case for degenerate paths
		os.gstate().stroke();
		os.gstate().newPath();
		return true;
	}
}

class Fill extends PaintingOperator {

	@Override
	public boolean execute(OperandStack os) {
		// FREEHEP-156: add special case for degenerate paths
		os.gstate().fill();
		os.gstate().newPath();
		return true;
	}
}

class EOFill extends PaintingOperator {

	@Override
	public boolean execute(OperandStack os) {
		// FREEHEP-156: add special case for degenerate paths
		os.gstate().path().setWindingRule(Path.WIND_EVEN_ODD);
		os.gstate().fill();
		os.gstate().newPath();
		return true;
	}
}

class RectStroke extends PaintingOperator {
	{
		operandTypes = new Class[] { PSObject.class };
	}

	private static void stroke(OperandStack os, double x, double y, double w,
			double h, org.freehep.vectorgraphics.Transform m) {
		Rectangle r = os.gstate().device().createRectangle(x, y, w, h);
		os.gstate().stroke(r, m);
	}

	private static void stroke(OperandStack os, PSPackedArray a,
			org.freehep.vectorgraphics.Transform m) {
		for (int i = 0; i < a.size() / 4; i++) {
			double x = ((PSNumber) a.get(i * 4)).getDouble();
			double y = ((PSNumber) a.get(i * 4 + 1)).getDouble();
			double w = ((PSNumber) a.get(i * 4 + 2)).getDouble();
			double h = ((PSNumber) a.get(i * 4 + 3)).getDouble();
			stroke(os, x, y, w, h, m);
		}
	}

	@Override
	public boolean execute(OperandStack os) {
		if (os.checkType(PSNumber.class, PSNumber.class, PSNumber.class,
				PSNumber.class)) {
			double h = os.popNumber().getDouble();
			double w = os.popNumber().getDouble();
			double y = os.popNumber().getDouble();
			double x = os.popNumber().getDouble();
			stroke(os, x, y, w, h, null);
		} else if (os.checkType(PSNumber.class, PSNumber.class, PSNumber.class,
				PSNumber.class, PSPackedArray.class)) {
			org.freehep.vectorgraphics.Transform m = os.gstate().device()
					.createTransform(os.popPackedArray().toDoubles());
			double h = os.popNumber().getDouble();
			double w = os.popNumber().getDouble();
			double y = os.popNumber().getDouble();
			double x = os.popNumber().getDouble();
			stroke(os, x, y, w, h, m);
		} else if (os.checkType(PSPackedArray.class, PSPackedArray.class)) {
			org.freehep.vectorgraphics.Transform m = os.gstate().device()
					.createTransform(os.popPackedArray().toDoubles());
			PSPackedArray a = os.popPackedArray();
			stroke(os, a, m);
		} else if (os.checkType(PSPackedArray.class)) {
			PSPackedArray a = os.popPackedArray();
			stroke(os, a, null);
		} else {
			// FIXME: encoded strings not handled
			error(os, new TypeCheck());
		}
		return true;
	}
}

class RectFill extends PaintingOperator {
	{
		operandTypes = new Class[] { PSObject.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		if (os.checkType(PSNumber.class, PSNumber.class, PSNumber.class,
				PSNumber.class)) {
			double h = os.popNumber().getDouble();
			double w = os.popNumber().getDouble();
			double y = os.popNumber().getDouble();
			double x = os.popNumber().getDouble();
			Rectangle r = os.gstate().device().createRectangle(x, y, w, h);
			os.gstate().fill(r);
		} else if (os.checkType(PSPackedArray.class)) {
			PSPackedArray a = os.popPackedArray();
			for (int i = 0; i < a.size() / 4; i++) {
				double x = ((PSNumber) a.get(i * 4)).getDouble();
				double y = ((PSNumber) a.get(i * 4 + 1)).getDouble();
				double w = ((PSNumber) a.get(i * 4 + 2)).getDouble();
				double h = ((PSNumber) a.get(i * 4 + 3)).getDouble();
				Rectangle r = os.gstate().device().createRectangle(x, y, w, h);
				os.gstate().fill(r);
			}
		} else {
			// FIXME: encoded strings not handled
			error(os, new TypeCheck());
		}
		return true;
	}
}

class UStroke extends PaintingOperator {
	private boolean done;
	private org.freehep.vectorgraphics.Transform matrix;

	private UStroke(boolean d, org.freehep.vectorgraphics.Transform m) {
		done = d;
		matrix = m;
	}

	public UStroke() {
	}

	@Override
	public boolean execute(OperandStack os) {
		if (!done) {
			if (!os.checkType(PSPackedArray.class)) {
				error(os, new TypeCheck());
				return true;
			}

			final int matrixSize = 6;
			org.freehep.vectorgraphics.Transform m = null;
			PSPackedArray proc = os.popPackedArray();
			if (proc.size() == matrixSize) {
				try {
					m = os.gstate().device().createTransform(proc.toDoubles());
					if (!os.checkType(PSPackedArray.class)) {
						error(os, new TypeCheck());
						return true;
					}
					proc = os.popPackedArray();
				} catch (ClassCastException e) {
					// no matrix
				}
			}

			// gsave, newpath, systemdict, begin
			os.gsave();
			os.gstate().newPath();
			os.dictStack().push(os.dictStack().systemDictionary());
			PSPackedArray upath = (PSPackedArray) proc.copy();
			upath.setExecutable();

			os.execStack().pop();
			os.execStack().push(new UStroke(true, m));
			os.execStack().push(upath);
			return false;
		}

		// upath was executed, end, stroke and grestore
		os.dictStack().pop();
		if (matrix != null) {
			os.gstate().transform(matrix);
		}
		os.gstate().stroke();
		os.grestore();
		return true;
	}
}

class UFill extends PaintingOperator {
	private boolean done;

	private UFill(boolean d) {
		done = d;
	}

	public UFill() {
	}

	@Override
	public boolean execute(OperandStack os) {
		if (!done) {
			if (!os.checkType(PSPackedArray.class)) {
				error(os, new TypeCheck());
				return true;
			}

			PSPackedArray proc = os.popPackedArray();

			// gsave, newpath, systemdict, begin
			os.gsave();
			os.gstate().newPath();
			os.dictStack().push(os.dictStack().systemDictionary());
			PSPackedArray upath = (PSPackedArray) proc.copy();
			upath.setExecutable();

			os.execStack().pop();
			os.execStack().push(new UFill(true));
			os.execStack().push(upath);
			return false;
		}

		// upath was executed, end, fill and grestore
		os.dictStack().pop();
		os.gstate().fill();
		os.grestore();
		return true;
	}
}

class UEOFill extends PaintingOperator {
	private boolean done;

	private UEOFill(boolean d) {
		done = d;
	}

	public UEOFill() {
	}

	@Override
	public boolean execute(OperandStack os) {
		if (!done) {
			if (!os.checkType(PSPackedArray.class)) {
				error(os, new TypeCheck());
				return true;
			}

			PSPackedArray proc = os.popPackedArray();

			// gsave, newpath, systemdict, begin
			os.gsave();
			os.gstate().newPath();
			os.dictStack().push(os.dictStack().systemDictionary());
			PSPackedArray upath = (PSPackedArray) proc.copy();
			upath.setExecutable();

			os.execStack().pop();
			os.execStack().push(new UEOFill(true));
			os.execStack().push(upath);
			return false;
		}

		// upath was executed, end, eofill and grestore
		os.dictStack().pop();
		os.gstate().path().setWindingRule(Path.WIND_EVEN_ODD);
		os.gstate().fill();
		os.grestore();
		return true;
	}
}

class ShFill extends PaintingOperator {
	{
		operandTypes = new Class[] { PSDictionary.class };
	}

	@Override
	public boolean execute(OperandStack os) {
		// Level 3 PostScript
		error(os, new Unimplemented());
		return true;
	}
}

class ImageOperator extends PaintingOperator {

	private boolean imageMask;
	private int width;
	private int height;
	private org.freehep.vectorgraphics.Transform matrix;
	private PSPackedArray[] proc;
	private PSDataSource[] source;
	private boolean multi;
	private int bitsPerComponent;
	private PSPackedArray decode;
	protected boolean interpolate;

	private final int dataSize = 8;
	private Image image;
	private int pixelBitStride;
	private int scanlineStride;
	private int components;
	private float[] color;
	private int[] pixels = new int[4];
	private int x, y, c;
	private boolean popString;

	protected ImageOperator() {
	}

	protected ImageOperator(PSDevice device, boolean mask, int w, int h, int b,
			PSPackedArray m, PSObject[] ds, PSPackedArray d, boolean i) {

		imageMask = mask;
		width = w;
		height = h;
		switch (b) {
		case 1:
		case 2:
		case 4:
		case 8:
			// case 12: // FIXME: probably does not work
			bitsPerComponent = b;
			break;
		default:
			throw new IllegalArgumentException();
		}
		matrix = device.createTransform(m.toDoubles());

		multi = (ds.length > 1);
		proc = new PSPackedArray[ds.length];
		source = new PSDataSource[ds.length];
		if (ds[0] instanceof PSPackedArray) {
			for (int k = 0; k < ds.length; k++) {
				proc[k] = (PSPackedArray) ds[k];
			}
		} else if (ds[0] instanceof PSDataSource) {
			for (int k = 0; k < ds.length; k++) {
				source[k] = (PSDataSource) ds[k];
			}
		} else {
			throw new IllegalArgumentException();
		}

		decode = d;
		components = decode.size() / 2;
		color = new float[components];
		interpolate = i;

		image = device.createImage(width, height, Image.TYPE_INT_ARGB);
		pixelBitStride = bitsPerComponent * components;
		scanlineStride = (int) Math.ceil(width * pixelBitStride / dataSize);
		popString = false;
		x = 0;
		y = 0;
		c = 0;
	}

	public boolean handleImageParameters(OperandStack os, boolean mask) {
		if (os.checkType(PSDictionary.class)) {
			PSDictionary dict = os.popDictionary();
			try {
				int type = dict.getInteger("ImageType");
				switch (type) {
				case 1:
					int w = dict.getInteger("Width");
					int h = dict.getInteger("Height");
					PSPackedArray m = dict.getPackedArray("ImageMatrix");
					PSObject multiDataSources = dict.get("MultipleDataSources");
					PSObject[] ds;
					if ((multiDataSources != null)
							&& ((PSBoolean) multiDataSources).getValue()) {
						if (mask) {
							error(os, new RangeCheck());
							return true;
						}
						ds = dict.getPackedArray("DataSource").toObjects();
					} else {
						ds = new PSObject[1];
						ds[0] = dict.get("DataSource");
					}
					int b = dict.getInteger("BitsPerComponent");
					if (mask && (b != 1)) {
						error(os, new RangeCheck());
						return true;
					}
					PSPackedArray d = dict.getPackedArray("Decode");
					if (!mask) {
						if (d.size() != os.gstate()
								.getNumberOfColorSpaceComponents() * 2) {
							error(os, new RangeCheck());
							return true;
						}
					} /*
					 * else { // FIXME: check d being 1 0 or 0 1 }
					 */
					PSBoolean inter = (PSBoolean) dict.get("Interpolate");
					boolean i = (inter != null) ? inter.getValue() : false;

					ImageOperator imageOperator = new ImageOperator(os.gstate()
							.device(), mask, w, h, b, m, ds, d, i);
					os.execStack().pop();
					os.execStack().push(imageOperator);
					return false;
				default:
					// Level 3
					error(os, new Undefined());
					break;
				}
			} catch (NullPointerException e) {
				log.log(Level.SEVERE, e.getMessage(), e.getStackTrace());
				error(os, new Undefined());
			} catch (IllegalArgumentException e) {
				error(os, new Undefined());
			}
			return true;

		} else if (os.checkType(PSInteger.class, PSInteger.class,
				PSObject.class, PSPackedArray.class, PSObject.class)) {
			PSObject[] ds = new PSObject[1];
			ds[0] = os.popObject();
			PSPackedArray m = os.popPackedArray();
			int b;
			PSPackedArray d;
			if (mask) {
				b = 1;
				if (os.popBoolean().getValue()) {
					d = new PSPackedArray(new double[] { 1, 0 });
				} else {
					d = new PSPackedArray(new double[] { 0, 1 });
				}
			} else {
				// image is always DeviceGray
				b = os.popInteger().getValue();
				d = new PSPackedArray(new double[] { 0, 1 });
			}
			int h = os.popInteger().getValue();
			int w = os.popInteger().getValue();

			try {
				ImageOperator imageOperator = new ImageOperator(os.gstate().device(), mask, w, h, b,
						m, ds, d, false);
				os.execStack().pop();
				os.execStack().push(imageOperator);
			} catch (IllegalArgumentException e) {
				error(os, new TypeCheck());
				return true;
			} catch (ClassCastException e) {
				error(os, new TypeCheck());
				return true;
			}

			return false;
		} else {

			error(os, new TypeCheck());
			return true;
		}
	}

	@Override
	public boolean execute(OperandStack os) {
		boolean eod = false;

		// pop the prepared string
		if ((proc[c] != null) && popString) {
			// store string
			if (!os.checkType(PSString.class)) {
				error(os, new TypeCheck());
			}
			PSString s = os.popString();
			if (s.size() <= 0) {
				eod = true;
			}
			source[c] = s;
		}

		int b = -1;
		int[] prevIndex = new int[4];
		for (int i = 0; i < prevIndex.length; i++) {
			prevIndex[i] = -1;
		}
		// fill part of the image
		try {
			// file or string is filled
			while ((y < height) && !eod) {
				while ((x < width) && !eod) {
					while ((c < components) && !eod) {
						int bitnum = x * pixelBitStride + c * bitsPerComponent;
						int index = y * scanlineStride + bitnum / dataSize;
						int shift = dataSize - (bitnum & (dataSize - 1))
								- bitsPerComponent;
						int s = (multi) ? c : 0;

						if (index > prevIndex[s]) {
							prevIndex[s] = index;
							if (proc[s] != null) {
								b = (source[s] != null) ? source[s].read() : -1;
								if (b < 0) {
									popString = true;
									PSPackedArray p = (PSPackedArray) proc[s]
											.clone();
									os.execStack().push(p);
									return false;
								}
							} else {
								b = source[s].read();
								if (b < 0) {
									if (source[s] instanceof PSString) {
										source[s].reset();
										b = source[s].read();
									} else {
										eod = true;
									}
								}
							}
						}

						int pixel = (b >> shift)
								& ((1 << bitsPerComponent) - 1);
						float dMin = ((PSNumber) decode.get(c * 2 + 0))
								.getFloat();
						float dMax = ((PSNumber) decode.get(c * 2 + 1))
								.getFloat();
						color[c] = dMin
								+ (pixel * (dMax - dMin) / ((1 << bitsPerComponent) - 1));
						c++;
					}
					float[] rgb;
					float alpha;
					if (imageMask) {
						// image mask
						rgb = PSGState.toRGB(os.gstate().color(), os.gstate()
								.colorSpace());
						alpha = (color[0] == 0) ? 1.0f : 0.0f;
					} else {
						// normal image
						switch (components) {
						case 1:
							rgb = PSGState.toRGB(color, "DeviceGray");
							break;
						case 3:
							rgb = PSGState.toRGB(color, "DeviceRGB");
							break;
						case 4:
							rgb = PSGState.toRGB(color, "DeviceCMYK");
							break;
						default:
							error(os, new RangeCheck(getClass().getName()
									+ ": expected length [1..4], was "
									+ components));
							return true;
						}
						alpha = 1.0f;
					}
					pixels[0] = (int) (rgb[0] * 255);
					pixels[1] = (int) (rgb[1] * 255);
					pixels[2] = (int) (rgb[2] * 255);
					pixels[3] = (int) (alpha * 255);
					image.setPixel(x, y, pixels);
					c = 0;
					x++;
				}
				x = 0;
				y++;
			}

			// FIXME: ignores interpolate parameter
			os.gstate().image(image, matrix);
		} catch (IOException e) {
			error(os, new IOError(e));
		} catch (ClassCastException e) {
			error(os, new TypeCheck());
		} catch (CloneNotSupportedException e) {
			error(os, new Unimplemented());
		}

		return true;
	}
}

class PaintImage extends ImageOperator {

	@Override
	public String getName() {
		return "image";
	}

	@Override
	public boolean execute(OperandStack os) {
		return handleImageParameters(os, false);
	}
}

class ColorImage extends ImageOperator {

	@Override
	public boolean execute(OperandStack os) {
		if (!os.checkType(PSBoolean.class, PSInteger.class)) {
			error(os, new TypeCheck());
			return true;
		}

		int ncomp = os.popInteger().getValue();
		boolean multi = os.popBoolean().getValue();

		PSPackedArray d;
		switch (ncomp) {
		case 1:
			d = new PSPackedArray(new float[] { 0, 1 });
			break;
		case 3:
			d = new PSPackedArray(new float[] { 0, 1, 0, 1, 0, 1 });
			break;
		case 4:
			d = new PSPackedArray(new float[] { 0, 1, 0, 1, 0, 1, 0, 1 });
			break;
		default:
			error(os, new RangeCheck());
			return true;
		}

		PSObject[] ds;
		if (multi) {
			ds = new PSObject[ncomp];
			for (int i = ncomp - 1; i >= 0; i--) {
				ds[i] = os.popObject();
			}
		} else {
			ds = new PSObject[1];
			ds[0] = os.popObject();
		}

		if (!os.checkType(PSInteger.class, PSInteger.class, PSInteger.class,
				PSPackedArray.class)) {
			error(os, new TypeCheck());
			return true;
		}

		PSPackedArray m = os.popPackedArray();
		int b = os.popInteger().getValue();
		int h = os.popInteger().getValue();
		int w = os.popInteger().getValue();

		try {
			ImageOperator ci = new ImageOperator(os.gstate().device(), false, w, h, b, m, ds, d,
					false);
			os.execStack().pop();
			os.execStack().push(ci);
			return false;
		} catch (IllegalArgumentException e) {
			error(os, new TypeCheck());
			return true;
		} catch (ClassCastException e) {
			error(os, new TypeCheck());
			return true;
		}
	}
}

class ImageMask extends ImageOperator {

	@Override
	public boolean execute(OperandStack os) {
		return handleImageParameters(os, true);
	}
}
