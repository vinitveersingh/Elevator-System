package main;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DrawGUI extends JPanel{
	public static ArrayList<Elevator> elevators;
	public static int num_floors = 0;
    
    public DrawGUI(ArrayList<Elevator> e, int floors) {
        elevators = e;
        num_floors = floors;
    }
    
    public void paintComponent(Graphics g) {
    	super.paintComponent(g);
        int width = getSize().width;
        int height = getSize().height;
        int padding = width/100;
        int padding_vertical = height/160;
        int nfloors = num_floors;
        int rowHeight = height/nfloors;
        int colWidth = width/(elevators.size()+1);
        g.setColor(Color.white);
        int diameter = Math.min(colWidth, rowHeight) - 2*padding;
        
        //levels and buttons
    	for (int k=0; k<nfloors; ++k) {
        	for (int i=0; i<elevators.size(); ++i) {
	            int level = rowHeight * (nfloors-k-1);
	            g.setColor(Color.black);
	            g.drawLine(0, level, width, level);
	            
	            /*//up-buttons
	            g.setColor(RequestListener.getExternalLight(k, 0) == true?Color.yellow:Color.lightGray);
	            if(k!=nfloors-1)
	            	g.fillOval(elevators.size()*colWidth+padding*padding, level+padding_vertical, diameter/3, diameter/3);
	            g.setColor(Color.black);
	            g.drawOval(elevators.size()*colWidth+padding*padding, level+padding_vertical, diameter/3, diameter/3);
	            g.drawString("^", elevators.size()*colWidth+(padding+2)*padding, level+rowHeight/3-padding_vertical);
	            
	            //down-buttons
	            g.setColor(RequestListener.getExternalLight(k, 1) == true?Color.yellow:Color.lightGray);
	            if(k!=0)
	            	g.fillOval(elevators.size()*colWidth+padding*padding, level+padding_vertical+diameter/3, diameter/3, diameter/3);
	            g.setColor(Color.black);
	            g.drawOval(elevators.size()*colWidth+padding*padding, level+padding_vertical+diameter/3, diameter/3, diameter/3);
	            g.drawString("v", elevators.size()*colWidth+(padding+2)*padding, level+rowHeight/3+diameter/3-padding);*/
        	}
    	}
    	
    	//elevators
        for (double j=0; j<=nfloors-1; j=Util.round((j+0.1),1)) {
        	for (int i=0; i<elevators.size(); ++i) {
	            int elev_level = (int)(rowHeight * (nfloors-j-1));
	            
	            //elevators' locations
	            if (elevators.get(i).getCurrentFloor() == j) {
	            	int doorSize = elevators.get(i).getDoorSize();
	            	g.setColor(Color.lightGray);
	                g.fillRect(i*colWidth+3*padding, elev_level+padding, diameter-2*padding, diameter);
	                g.setColor(Color.black);
	                g.drawRect(i*colWidth+3*padding, elev_level+padding, diameter-2*padding, diameter);
	                g.drawRect(i*colWidth+3*padding, elev_level+padding, diameter/2-padding, diameter);
	                if(doorSize>0) {
	                	g.setColor(Color.black);
	                	g.fillRect(i*colWidth+3*padding+8*(padding-doorSize), elev_level+padding, (int)((doorSize/4.0)*(diameter-2*padding)), diameter);
	                }
	                g.setColor(Color.red);
	                //floor labels
	                g.drawString(""+(int)j, i*colWidth+(padding+2)*padding, elev_level+rowHeight/3-(padding-2));
	            }
        	}
        }
        
    }
    
    public static void initGraphics(ArrayList<Elevator> elevators, int floors) {
        
        RequestListener listener = new RequestListener(elevators, floors);
    	
        JFrame frame = new JFrame("Elevator System");
        frame.setSize(500, 800);

        DrawGUI gui = new DrawGUI(elevators, floors);
        
        frame.setLayout(new BorderLayout());
        
        frame.add(gui, "Center");
        
        JPanel panel_ext_buttons = new JPanel();
        
        panel_ext_buttons.setLayout(new GridLayout(0,1,0,8));
        extButtons(panel_ext_buttons, listener);
        
        frame.add(panel_ext_buttons, "East");
        
        frame.setLocation(500, 50);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        
        JPanel panel_int_buttons = new JPanel();
        JFrame btn_frame = new JFrame("Interior Buttons");
        btn_frame.setSize(500, 400);
        
        panel_int_buttons.setLayout(new GridLayout(elevators.size(),1,2,25));
        intButtons(panel_int_buttons, listener);
        
        btn_frame.add(panel_int_buttons);
        
        btn_frame.setLocation(1000, 50);
        btn_frame.setVisible(true);
        btn_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        btn_frame.setResizable(false);
        
        JPanel panel_int_lights = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // code here
                int width = getSize().width;
                int height = getSize().height;
                int padding = width/100;
                int nelevs = elevators.size();
                int nfloors = num_floors;
                int rowHeight = height/nelevs;
                int colWidth = width/(nfloors+1);
                g.setColor(Color.white);
                int diameter = Math.min(colWidth, rowHeight) - 2*padding;
                for (int j=0; j<nelevs; ++j) {
                	for (int i=0; i<nfloors; ++i) {
        	            int level = rowHeight * (nelevs-j-1);
        	            g.setColor(Color.black);
        	            g.drawLine(0, level, width, level);
        	            
        	            //up-buttons
        	            g.setColor(RequestListener.getInternalLight(nelevs-j-1, i) == true?Color.yellow:Color.lightGray);
        	            g.fillOval(i*colWidth+3*padding, level+padding, diameter, diameter);
        	            g.setColor(Color.black);
        	            g.drawOval(i*colWidth+3*padding, level+padding, diameter, diameter);
        	            g.drawString(""+i, i*colWidth+(padding+2)*padding, level+rowHeight/3-(padding-2));
        	            
                	}
                }
            }
        };
        
        /*JFrame btn_lights = new JFrame("Interior Lights");
        btn_lights.setSize(500, 400);
        
        
        btn_lights.add(panel_int_lights);
        
        btn_lights.setLocation(1000, 450);
        btn_lights.setVisible(true);
        btn_lights.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        btn_lights.setResizable(false);*/
        
        //keep animating
        while(true)
        	animate(gui,panel_int_lights);
        
    }
    
    public static void extButtons(JPanel panel, RequestListener listener) {
        
        int floors = num_floors;

    	JButton[] ext_up = new JButton[floors];
    	JButton[] ext_down = new JButton[floors];
    	
    	for (int i = 0; i < floors; i++){
    		
    		ext_up[i] = new JButton("^");
			ext_up[i].setBackground(Color.gray);
			ext_up[i].setName((floors-1-i)+"ue");
			ext_up[i].addActionListener(listener);
			
			panel.add(ext_up[i]);
    		if(i==0) {
    			ext_up[i].setVisible(false);
    			ext_up[i].setEnabled(false);
    		}
    		
    		ext_down[i] = new JButton("v");
			ext_down[i].setBackground(Color.gray);
			ext_down[i].setName((floors-1-i)+"de");
			ext_down[i].addActionListener(listener);

			panel.add(ext_down[i]);

    		if(i==floors-1) {
    			ext_down[i].setVisible(false);
    			ext_down[i].setEnabled(false);
    		}
			
    	}

    }
    
    public static void intButtons(JPanel panel, RequestListener listener) {
        
        int floors = num_floors;
        int num_elevators = elevators.size();

    	JButton[][] interior = new JButton[num_elevators][floors];
    	JLabel[] label = new JLabel[num_elevators];
    	
    	for (int i = 0; i < num_elevators; i++){
    		label[i] = new JLabel("Elev:" + i);
    		panel.add(label[i]);
    		for (int j = 0; j < floors; j++){
    		
	    		interior[i][j] = new JButton(""+j);
    			interior[i][j].setBackground(Color.gray);
				
				interior[i][j].setName(i+""+j+"i");
				interior[i][j].addActionListener(listener);
				
				panel.add(interior[i][j]);
	    		/*if(i==0) {
	    			interior[i][j].setEnabled(false);
	    		}*/
    		}
    		
    	}

    }

    
    static void animate(JPanel p, JPanel q) {
        try {
            Thread.sleep(250);
        } catch (Exception ex) {};
        p.repaint();
        q.repaint();
    }

    
    
}
