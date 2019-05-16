package com.zkxltech.ui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class MessageShell {

    /**
     * Launch the application.
     * @param args
     */
//	public static void main(String args[]) {
//		try {
//			Display display = Display.getDefault();
//			MessageShell shell = new MessageShell(display);
//			shell.open();
//			shell.layout();
//			while (!shell.isDisposed()) {
//				if (!display.readAndDispatch()) {
//					display.sleep();
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

    /**
     * Create the shell.
     * @param display
     */
    public MessageShell(String text) {
        Display display = Display.getDefault();
        display.syncExec(new Runnable() {
            @Override
            public void run() {
                Shell shell = new Shell(display);
                createContents(shell,text);
            }
        });

    }

    /**
     * Create contents of the shell.
     */
    protected void createContents(Shell shell,String text) {
        shell.setText("SWT Application");
        shell.setSize(450, 300);
        MessageBox messageBox = new MessageBox(shell);
        messageBox.setMessage(text);
        messageBox.open();
    }
}
