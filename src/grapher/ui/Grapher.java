package grapher.ui;

import java.awt.Button;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;

import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;
import java.util.Vector;

import static java.lang.Math.*;
import grapher.fc.*;


public class Grapher extends JPanel implements MouseInputListener, MouseWheelListener {
	static final int MARGIN = 40;
	static final int STEP = 5;

	static final BasicStroke dash = new BasicStroke(1, BasicStroke.CAP_ROUND,
	                                                   BasicStroke.JOIN_ROUND,
	                                                   1.f,
	                                                   new float[] { 4.f, 4.f },
	                                                   0.f);

	protected int W = 400;
	protected int H = 300;

	protected double xmin, xmax;
	protected double ymin, ymax;

	protected Vector<Function> functions;

	private Point m_firstMousePos;
	private Point m_lastMousePos;
	private int m_firstButtonPress;

	public Grapher() {
		xmin = -PI/2.; xmax = 3*PI/2;
		ymin = -1.5;   ymax = 1.5;
		m_firstButtonPress = 0;
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		functions = new Vector<Function>();
	}

	public void add(String expression) {
		add(FunctionFactory.createFunction(expression));
	}

	public void add(Function function) {
		functions.add(function);
		repaint();
	}

	public Dimension getPreferredSize() { return new Dimension(W, H); }

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		W = getWidth();
		H = getHeight();

		Graphics2D g2 = (Graphics2D)g;

		// background
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, W, H);

		g2.setColor(Color.BLACK);

		// box
		g2.translate(MARGIN, MARGIN);
		W -= 2*MARGIN;
		H -= 2*MARGIN;
		if(W < 0 || H < 0) {
			return;
		}

		g2.drawRect(0, 0, W, H);

		g2.drawString("x", W, H+10);
		g2.drawString("y", -10, 0);


		// plot
		g2.clipRect(0, 0, W, H);
		g2.translate(-MARGIN, -MARGIN);

		// x values
		final int N = W/STEP + 1;
		final double dx = dx(STEP);
		double xs[] = new double[N];
		int    Xs[] = new int[N];
		for(int i = 0; i < N; i++) {
			double x = xmin + i*dx;
			xs[i] = x;
			Xs[i] = X(x);
		}

		for(Function f : functions) {
			// y values
			int Ys[] = new int[N];
			for(int i = 0; i < N; i++) {
				Ys[i] = Y(f.y(xs[i]));
			}

			g2.drawPolyline(Xs, Ys, N);
		}

		g2.setClip(null);

		// axes
		drawXTick(g2, 0);
		drawYTick(g2, 0);

		double xstep = unit((xmax-xmin)/10);
		double ystep = unit((ymax-ymin)/10);

		g2.setStroke(dash);
		for(double x = xstep; x < xmax; x += xstep)  { drawXTick(g2, x); }
		for(double x = -xstep; x > xmin; x -= xstep) { drawXTick(g2, x); }
		for(double y = ystep; y < ymax; y += ystep)  { drawYTick(g2, y); }
		for(double y = -ystep; y > ymin; y -= ystep) { drawYTick(g2, y); }

		if (m_firstButtonPress == MouseEvent.BUTTON3 && !m_firstMousePos.equals(m_lastMousePos)) {
			g2.drawRect((int)m_firstMousePos.getX(), (int)m_firstMousePos.getY(), (int)(m_lastMousePos.getX() - m_firstMousePos.getX()), (int)(m_lastMousePos.getY() - m_firstMousePos.getY()));
		}

	}

	protected double dx(int dX) { return  (double)((xmax-xmin)*dX/W); }
	protected double dy(int dY) { return -(double)((ymax-ymin)*dY/H); }

	protected double x(int X) { return xmin+dx(X-MARGIN); }
	protected double y(int Y) { return ymin+dy((Y-MARGIN)-H); }

	protected int X(double x) {
		int Xs = (int)round((x-xmin)/(xmax-xmin)*W);
		return Xs + MARGIN;
	}
	protected int Y(double y) {
		int Ys = (int)round((y-ymin)/(ymax-ymin)*H);
		return (H - Ys) + MARGIN;
	}

	protected void drawXTick(Graphics2D g2, double x) {
		if(x > xmin && x < xmax) {
			final int X0 = X(x);
			g2.drawLine(X0, MARGIN, X0, H+MARGIN);
			g2.drawString((new Double(x)).toString(), X0, H+MARGIN+15);
		}
	}

	protected void drawYTick(Graphics2D g2, double y) {
		if(y > ymin && y < ymax) {
			final int Y0 = Y(y);
			g2.drawLine(0+MARGIN, Y0, W+MARGIN, Y0);
			g2.drawString((new Double(y)).toString(), 5, Y0);
		}
	}

	protected static double unit(double w) {
		double scale = pow(10, floor(log10(w)));
		w /= scale;
		if(w < 2)      { w = 2; }
		else if(w < 5) { w = 5; }
		else           { w = 10; }
		return w * scale;
	}


	protected void translate(int dX, int dY) {
		double dx = dx(dX);
		double dy = dy(dY);
		xmin -= dx; xmax -= dx;
		ymin -= dy; ymax -= dy;
		repaint();
	}

	protected void zoom(Point center, int dz) {
		double x = x(center.x);
		double y = y(center.y);
		double ds = exp(dz*.01);
		xmin = x + (xmin-x)/ds; xmax = x + (xmax-x)/ds;
		ymin = y + (ymin-y)/ds; ymax = y + (ymax-y)/ds;
		repaint();
	}

	protected void zoom(Point p0, Point p1) {
		double x0 = x(p0.x);
		double y0 = y(p0.y);
		double x1 = x(p1.x);
		double y1 = y(p1.y);
		xmin = min(x0, x1); xmax = max(x0, x1);
		ymin = min(y0, y1); ymax = max(y0, y1);
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		if (m_firstButtonPress == 0) {
			m_firstMousePos = arg0.getPoint();
			m_lastMousePos = m_firstMousePos;
			m_firstButtonPress = arg0.getButton();
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		if (m_firstButtonPress == arg0.getButton()) {
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			Point wCurrent = arg0.getPoint();
			int wButton = arg0.getButton();

			if(wCurrent.equals(m_firstMousePos) ) {

				if (wButton == MouseEvent.BUTTON1) { // clic gauche
					zoom(wCurrent,20);
				}
				if (wButton == MouseEvent.BUTTON3) { // clic droit
					zoom(wCurrent,-20);
				}

			} else {
				if (wButton == MouseEvent.BUTTON3) {
					zoom(m_firstMousePos,wCurrent);
				}
			}
			m_firstMousePos = null;
			m_lastMousePos = null;
			m_firstButtonPress = 0;
		}
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		if (m_firstButtonPress == MouseEvent.BUTTON1) {
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			translate((int)(arg0.getX() - m_lastMousePos.getX()), (int)(arg0.getY() - m_lastMousePos.getY()));
			m_lastMousePos = arg0.getPoint();
		} else if (m_firstButtonPress == MouseEvent.BUTTON3) {
			m_lastMousePos = arg0.getPoint();
			repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		if (arg0.getWheelRotation() < 0) {
			zoom(arg0.getPoint(), 5);
		} else {
			zoom(arg0.getPoint(), -5);
		}
	}


}
