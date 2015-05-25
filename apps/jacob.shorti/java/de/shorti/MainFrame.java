package de.shorti;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import de.shorti.baseknowledge.objects.CommunicationChannel;
import de.shorti.message.Message;
import de.shorti.message.QuestionResolver;

public class MainFrame extends JFrame
{
    JPanel contentPane;
    BorderLayout borderLayout1     = new BorderLayout();
    JTextField   questionTextField = new JTextField();
    JTextArea    answersTextArea   = new JTextArea();
    JScrollPane jScrollPane1 = new JScrollPane();
    CommunicationChannel   channel = null;

    /**Construct the frame*/
    public MainFrame()
    {
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try
        {
            jbInit();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    /**Component initialization*/
    private void jbInit() throws Exception
    {
        //Channel with SMS formated output in the GUI
        channel = CommunicationChannel.findByConnectionString("gui");
        if(channel==null)
            channel = CommunicationChannel.createInstance("gui","de.shorti.iochannel.gui.Channel",null);

        // GUI with Mail output to a.herz@sapmarkets.com
//        channel = CommunicationChannel.findByConnectionString("a.herz@sapmarkets.com");
//        if(channel==null)
//            channel = CommunicationChannel.createInstance("a.herz@sapmarkets.com","de.shorti.iochannel.mail.Channel",null);

        // NOMAL GUI channel
//        channel = CommunicationChannel.findByConnectionString("gui");
//        if(channel==null)
//            channel = CommunicationChannel.createInstance("gui",CommunicationChannel.GUI,null);

        // GUI with ViaVoice output
//        channel = CommunicationChannel.findByConnectionString("viavoice");
//        if(channel==null)
//            channel = CommunicationChannel.createInstance("viavoice",CommunicationChannel.AUDIO,null);

        questionTextField.setBackground(Color.lightGray);
        questionTextField.setCaretColor(SystemColor.textHighlightText);
        questionTextField.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                onQuestionTextFieldPressed(e);
            }
        });
        //setIconImage(Toolkit.getDefaultToolkit().createImage(MainFrame.class.getResource("[Your Icon]")));
        contentPane = (JPanel) this.getContentPane();
        contentPane.setLayout(borderLayout1);
        this.setSize(new Dimension(400, 300));
        this.setTitle("short-i");
        answersTextArea.setLineWrap(true);
        answersTextArea.setRequestFocusEnabled(false);
        answersTextArea.setCaretColor(SystemColor.activeCaptionText);
        answersTextArea.setDoubleBuffered(true);
        answersTextArea.setForeground(SystemColor.activeCaptionText);
        answersTextArea.setDisabledTextColor(Color.yellow);
        answersTextArea.setBackground(Color.black);
        answersTextArea.setEditable(false);
        jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        contentPane.add(questionTextField, BorderLayout.SOUTH);
        contentPane.add(jScrollPane1, BorderLayout.CENTER);
        jScrollPane1.getViewport().add(answersTextArea, null);
    }
    /**Overridden so we can exit when window is closed*/
    protected void processWindowEvent(WindowEvent e)
    {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING)
        {
            Shorti.exit();
        }
    }

    void onQuestionTextFieldPressed(ActionEvent e)
    {

        Message message = new Message("TXT", channel,questionTextField.getText().trim().toLowerCase());
        QuestionResolver.process(message);
        if(message.getSendResponse())
        {
            answersTextArea.selectAll();
            answersTextArea.replaceSelection("");
            answersTextArea.append(message.getResponse()+"\n");
            message.setStatusSuccess();
//            MessageManager.send(message);
        }
        else
        {
            answersTextArea.selectAll();
            answersTextArea.replaceSelection("-");
        }
        message.setFinishTime();
    }
}