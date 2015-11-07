import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;



public class GUITest {
	public static GUI g;
	public static void main(String[] args) {
		
		g=new GUI();
		editContent();
		addMenuBar();
	}
	
	public static void editContent(){
		
		g.jpanel.setLayout(new BorderLayout());
		
		JButton b1=new JButton("WEST");
		JButton b2=new JButton("EAST");
		JButton b3=new JButton("NORTH");
		JButton b4=new JButton("SOUTH");
		JButton b5=new JButton("CENTER");
		
		g.jpanel.add(b1, BorderLayout.WEST);
		g.jpanel.add(b2, BorderLayout.EAST);
		g.jpanel.add(b3, BorderLayout.NORTH);
		g.jpanel.add(b4, BorderLayout.SOUTH);
		g.jpanel.add(b5, BorderLayout.CENTER);
	}
	
	public static void addMenuBar(){
	
		JMenuBar menubar=new JMenuBar();
		JMenu game = new JMenu("Game");
		JMenuItem start = new JMenuItem("Start New Game");
		
		start.addActionListener(new ActionListener(){
	@Override
		public void actionPerformed(ActionEvent e) {
		    JOptionPane.showMessageDialog(null, "Start new game!", "Start Game", JOptionPane.ERROR_MESSAGE);
		}
			
		});
		
		menubar.add(game);
		game.add(start);
		
		g.setJMenuBar(menubar);
		
	}

}
