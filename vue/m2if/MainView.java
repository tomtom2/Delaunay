package m2if;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

public class MainView extends JFrame implements Observer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private MyPanel panel;
	private ArrayList<Point> points = new ArrayList<Point>();
	private ArrayList<Triangle> triangulation = new ArrayList<Triangle>();
	
	private Delaunay triangulator = new Delaunay();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainView frame = new MainView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenuItem mntmClear = new JMenuItem("Clear");
		menuBar.add(mntmClear);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		panel = new MyPanel();
		panel.setBackground(Color.WHITE);
		scrollPane.setViewportView(panel);
		
		triangulator.addObserver(this);
		
		panel.addMouseListener(new MouseListener(){

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
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				addPoint(arg0.getX(), arg0.getY());
			}
			
		});
	}

	@Override
	public void update(ArrayList<Triangle> triangulation) {
		this.triangulation = triangulation;
		panel.repaint();
	}
	
	public void addPoint(int x, int y){
		Point p = new Point(x, y);
		points.add(p);
		panel.repaint();
		this.triangulator.addPoint(p);
	}
	
	
	
	public class MyPanel extends JPanel {
		public void paintComponent(Graphics g) {
			g.setColor( getBackground() );
			g.fillRect (0, 0, getWidth(), getHeight());

		    g.setColor(Color.black);
		    for(Point p : points)
		    	g.fillOval(p.getX(), p.getY(), 5, 5);
		    for(Triangle t : triangulation){
		    	g.drawLine(t.getP1().getX(),t.getP1().getY(),t.getP2().getX(),t.getP2().getY());
		    	g.drawLine(t.getP2().getX(),t.getP2().getY(),t.getP3().getX(),t.getP3().getY());
		    	g.drawLine(t.getP3().getX(),t.getP3().getY(),t.getP1().getX(),t.getP1().getY());
		    }
		  }
	}

}
