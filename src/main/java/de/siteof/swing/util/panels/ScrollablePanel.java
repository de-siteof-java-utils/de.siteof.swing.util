package de.siteof.swing.util.panels;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;
import javax.swing.SwingUtilities;

public class ScrollablePanel extends JPanel {

	private static class ScrollablePanelWrapper extends JPanel implements Scrollable {
//		private Component panel;
		public ScrollablePanelWrapper(Component panel) {
			super(new BorderLayout());
			this.add(panel, BorderLayout.CENTER);
//			this.panel = panel;
		}

		@Override
		public Dimension getPreferredScrollableViewportSize() {
			return this.getPreferredSize();
		}
		@Override
		public int getScrollableBlockIncrement(Rectangle visibleRect,
				int orientation, int direction) {
			return 20;
		}
		@Override
		public boolean getScrollableTracksViewportHeight() {
			return false;
		}
		@Override
		public boolean getScrollableTracksViewportWidth() {
			return false;
		}
		@Override
		public int getScrollableUnitIncrement(Rectangle visibleRect,
				int orientation, int direction) {
			return 20;
		}
	}

	private final JScrollPane scrollPane;

	private Point pressedPoint;
	private Point originalScrollPoint;
	private Dimension previousViewSize;

	public ScrollablePanel(final Component panel) {
		this(panel, true);
	}

	public ScrollablePanel(final Component panel, boolean contentDraggable) {
		super(new BorderLayout());
		if (panel instanceof Scrollable) {
			scrollPane	= new JScrollPane(panel);
		} else {
			scrollPane	= new JScrollPane(new ScrollablePanelWrapper(panel));
		}
		this.add(scrollPane, BorderLayout.CENTER);
		if (contentDraggable) {
			panel.addMouseListener(new MouseListener() {

				public void mouseClicked(MouseEvent event) {
					if (event.getClickCount() > 1) {
						setScrollPosition(originalScrollPoint);
					}
				}

				public void mouseEntered(MouseEvent event) {
				}

				public void mouseExited(MouseEvent event) {
				}

				public void mousePressed(MouseEvent event) {
					pressedPoint	= getAbsoluteMousePostion(event);
					originalScrollPoint = getScrollPosition();
					scrollPane.getViewport().getView().requestFocus();
				}

				public void mouseReleased(MouseEvent event) {
					updateScrollPosition(getAbsoluteMousePostion(event));
					pressedPoint	= null;
					originalScrollPoint = null;
				}});

			panel.addMouseMotionListener(new MouseMotionListener() {

				public void mouseDragged(MouseEvent event) {
					updateScrollPosition(getAbsoluteMousePostion(event));
				}

				public void mouseMoved(MouseEvent event) {
					updateScrollPosition(getAbsoluteMousePostion(event));
				}});

			panel.addKeyListener(new KeyListener() {

				public void keyPressed(KeyEvent event) {
				}

				public void keyReleased(KeyEvent event) {
				}

				public void keyTyped(KeyEvent event) {
					switch (event.getKeyCode()) {
					case KeyEvent.VK_LEFT:
						moveScrollPosition(-5, 0);
						break;
					case KeyEvent.VK_RIGHT:
						moveScrollPosition(5, 0);
						break;
					case KeyEvent.VK_UP:
						moveScrollPosition(0, -5);
						break;
					case KeyEvent.VK_DOWN:
						moveScrollPosition(0, 5);
						break;
					}
				}});
		};
		panel.addComponentListener(new ComponentListener() {

			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void componentResized(ComponentEvent e) {
				// TODO Auto-generated method stub
				Dimension size = panel.getPreferredSize();
				if ((previousViewSize == null) || (!previousViewSize.equals(size))) {
					previousViewSize = size;
					scrollPane.getViewport().getView().setSize(size);
//					ScrollablePanel.this.invalidate();
//					ScrollablePanel.this.doLayout();
					scrollPane.invalidate();
//					scrollPane.doLayout();
				}
			}

			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub

			}});
	}

	private Point getAbsoluteMousePostion(MouseEvent event) {
//		return SwingUtilities.convertPointToScreen(event.getPoint(), event.getComponent());
		return SwingUtilities.convertPoint(event.getComponent(), event.getPoint(), null);
//		return new Point(
//				event.getX() - scrollPane.getHorizontalScrollBar().getValue(),
//				event.getY() - scrollPane.getVerticalScrollBar().getValue());
	}

	private Point getScrollPosition() {
		return new Point(
				scrollPane.getHorizontalScrollBar().getValue(),
				scrollPane.getVerticalScrollBar().getValue());
//		return scrollPane.getViewport().getViewPosition();
	}

	private void setScrollPosition(Point point) {
		if (point != null) {
//			Dimension viewportSize = scrollPane.getViewport().getSize();
//			Dimension viewSize = scrollPane.getViewport().getViewSize();
			JScrollBar horizontalScrollBar = scrollPane.getHorizontalScrollBar();
			JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
			point = new Point(
					Math.max(horizontalScrollBar.getMinimum(),
							Math.min(horizontalScrollBar.getMaximum() -
									horizontalScrollBar.getVisibleAmount(), point.x)),
							Math.max(verticalScrollBar.getMinimum(),
									Math.min(verticalScrollBar.getMaximum() -
											verticalScrollBar.getVisibleAmount(), point.y)));
			horizontalScrollBar.setValue(point.x);
			verticalScrollBar.setValue(point.y);
			scrollPane.getViewport().setViewPosition(point);
		}
	}

	private void moveScrollPosition(int moveX, int moveY) {
		Point scrollPosition = this.getScrollPosition();
		if (scrollPosition != null) {
			setScrollPosition(new Point(scrollPosition.x + moveX, scrollPosition.y + moveY));
		}
	}

	private void updateScrollPosition(Point point) {
		if ((pressedPoint != null) && (originalScrollPoint != null)) {
			setScrollPosition(new Point(
					originalScrollPoint.x - point.x + pressedPoint.x,
					originalScrollPoint.y - point.y + pressedPoint.y));
		}
	}

}
