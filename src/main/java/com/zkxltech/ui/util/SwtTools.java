package com.zkxltech.ui.util;

import java.awt.Dimension;

import org.apache.poi.ss.formula.ptg.Pxg;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class SwtTools {
	static Cursor handCursor = new Cursor(Display.getDefault(), SWT.CURSOR_HAND);
	private static boolean top = false;
	private static boolean down = false;
	private static boolean left = false;
	private static boolean right = false;
	private static boolean drag = false;
	//移动窗口
	public static Listener MoveWindow(final Shell shell){
		Listener listener = new Listener() {
		    int startX, startY;
			
			public void handleEvent(Event e) {
				if (e.type == SWT.MouseDown && e.button == 1) {
		            startX = e.x;
		            startY = e.y;
		        }
		        if (e.type == SWT.MouseMove && (e.stateMask & SWT.BUTTON1) != 0) {
		            Point p = shell.toDisplay(e.x, e.y);
		            p.x -= startX;
		            p.y -= startY;
		            shell.setLocation(p);
		        }
			}
		};
		return listener;
	}
	
	//移动窗口
	public static Listener resetData(final Shell shell){
		Listener listener = new Listener() {
			public void handleEvent(Event e) {
		        if (e.type == SWT.MouseMove) {
		        	top = false;
		    		down = false;
		    		left = false;
		    		right = false;
		    		drag = false;
		    		shell.setCursor(new Cursor(Display.getDefault(), SWT.CURSOR_ARROW));
				}
			}
		};
		return listener;
	}
	
	
	//改变窗口大小
	public static Listener ChangeShellSize(final Shell shell){
		Listener listener = new Listener() {
			int lastX, lastY;
		    int startX, startY;
			public void handleEvent(Event e) {
				if (e.type == SWT.MouseDown && e.button == 1) {
					Point p = shell.toDisplay(e.x, e.y);
		            startX = e.x; 
		            startY = e.y;
		            lastX = p.x;
		            lastY = p.y - shell.getBounds().height;
		        }
		        if (e.type == SWT.MouseMove && (e.stateMask & SWT.BUTTON1) != 0) {
		           Point p = shell.toDisplay(e.x, e.y);
			       Rectangle r = shell.getBounds();
		            if (top) {
			            p.x -= startX;
						shell.setSize(r.width, r.height-e.y-1);
						shell.setLocation( p.x , p.y);
					} 
		            if (down) {
			            p.x -= startX;
			            p.y -= startY;
			            System.out.println(r.height-e.y-1);
						shell.setSize(r.width,  r.height-e.y-1);
						shell.setLocation( p.x , lastY);
					} 
		        }
		        if (e.type == SWT.MouseMove) {
			            if (e.y == 0) {
			            	shell.setCursor(new Cursor(Display.getDefault(), SWT.CURSOR_SIZEN));
							top = true;
						} else if (e.y == shell.getBounds().height-3) {
							shell.setCursor(new Cursor(Display.getDefault(), SWT.CURSOR_SIZEN));
							down = true;
						} else if (e.x == 0) {
							shell.setCursor(new Cursor(Display.getDefault(), SWT.CURSOR_SIZEE));
							left = true;
						} else if (e.x == shell.getBounds().width-3) {
							shell.setCursor(new Cursor(Display.getDefault(), SWT.CURSOR_SIZEE));
							left = true;
						}
				}
		        	
			}
		};
		return listener;
	}
	/**
	 * 
	 * 鼠标悬浮显示手图标
	 * 鼠标离开后恢复原来颜色
	 * @param cLabel
	 */
	
	public static MouseTrackListener showHand(final CLabel cLabel){
		MouseTrackListener mouseTrackListener =new MouseTrackListener() {
			
			public void mouseHover(MouseEvent e) {
			}
			
			public void mouseExit(MouseEvent e) {
				
			}
			
			public void mouseEnter(MouseEvent e) {
				cLabel.setCursor(handCursor);
				
			}
		};
		return mouseTrackListener;
	}
}
