package grapher.ui;

import javax.swing.JList;
import javax.swing.SwingUtilities;

import java.awt.Point;

public class enum Automata {

  static int ACCURACY_DRAG = 5;
  static int DEFAULT_ZOOM = 20;

  IDLE {
    public Automata press(MouseEvent arg0) {
      m_firstMousePos = arg0.getPoint();
      switch(arg0.getButton()) {
        case BUTTON1 :
          return LPRESS;
          break;
        case BUTTON3:
          return RPRESS;
          break;
        default:
          return IDLE;
      }
    }
  },
  LPRESS{
    public Automata move(MouseEvent arg0) {
      m_currentMousePos = arg0.getPoint();
      if(m_firstMousePos.distance(m_currentMousePos)>ACCURACY_DRAG) {
        m_grapher.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return LDRAG;
      } else {
        return LPRESS;
      }
    }

    public Automata release(MouseEvent arg0) {
      m_grapher.zoom(m_currentMousePos,DEFAULT_ZOOM);
      return IDLE;
    }
  },
  RPRESS{
    public Automata move(MouseEvent arg0) {
      m_currentMousePos = arg0.getPoint();
      if(m_firstMousePos.distance(m_currentMousePos)>ACCURACY_DRAG) {
        return RDRAG;
      } else {
        return RPRESS;
      }
    }

    public Automata release(MouseEvent arg0) {
      m_grapher.zoom(m_currentMousePos,-DEFAULT_ZOOM);
      return IDLE;
    }

  },
  LDRAG{
    public Automata move(MouseEvent arg0) {
      m_grapher.translate((int)(arg0.getX() - m_currentMousePos.getX()), (int)(arg0.getY() - m_currentMousePos.getY()));
      m_currentMousePos = arg0.getPoint();
      return LDRAG;
    }

    public Automata release(MouseEvent arg0) {
      m_grapher.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
      return IDLE;
    }
  },
  RDRAG{
    public Automata move(MouseEvent arg0) {
      m_currentMousePos = arg0.getPoint();
      return RDRAG;
    }

    public Automata release(MouseEvent arg0) {
      m_grapher.zoom(m_firstMousePos,m_currentMousePos);
      return IDLE;
    }
  };



  static Automata init(Grapher g) {
    m_grapher = g;
    return IDLE;
  }

  static Grapher m_grapher;
  private Point m_firstMousePos;
  private Point m_currentMousePos;

  public void draw(Graphics2D g2) {
    /* Drawing of the zoom tectangle */
    if (this == RDRAG) {
      double wX;
      double wY;
      double wWidth;
      double wHeight;

      wWidth = m_firstMousePos.getX() - m_currentMousePos.getX();
      /* Select the smallest x by checking if width is negative or not */
      if(wWidth < 0){
        wWidth = -wWidth;
        wX = m_firstMousePos.getX();
      }else{
        wX = m_currentMousePos.getX();
      }

      wHeight = m_firstMousePos.getY() - m_currentMousePos.getY();
      /* Select the smallest y by checking if width is negative or not */
      if(wHeight < 0){
        wHeight = -wHeight;
        wY = m_firstMousePos.getY();
      }else{
        wY = m_currentMousePos.getY();
      }

      g2.drawRect((int)wX,(int)wY,(int)wWidth,(int)wHeight);
    }
  }

  public Automata press(MouseEvent arg0) {
    //throw new RuntimeError();
  }

  public Automata move(MouseEvent arg0) {
    //throw new RuntimeError();
  }

  public Automata release(MouseEvent arg0) {
    //throw new RuntimeError();
  }
}
