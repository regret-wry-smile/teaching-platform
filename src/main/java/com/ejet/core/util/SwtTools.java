package com.ejet.core.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class SwtTools {
	static Cursor handCursor = new Cursor(Display.getDefault(), SWT.CURSOR_HAND);
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
