import java.awt.*;
import java.awt.event.*;
import javax.swing.Timer;
import java.io.File;
import javax.sound.sampled.*;

public class CountdownTimer extends Frame implements WindowListener{
    //time in timer (seconds)
    private int timerTime;

    //Clip for sound clip
    Clip clip;

    private Timer timer;
    private Label viewTimer;
    private TextField setHours, setMinutes, setSeconds;
    private Button startButton, stopButton;
    private final Font labelFont = new Font("SansSerif", Font.BOLD, 32);
    private final Font textFont = new Font("SansSerif", Font.BOLD, 20);

    public CountdownTimer(){
        //open the sound effect
        try {
            File soundfile = new File("Click.wav");
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundfile);
            clip = AudioSystem.getClip();
            clip.open(ais);
        }
        //print error if present
        catch(Exception e){
            e.printStackTrace();
        }


        setLayout(new GridLayout(3,1,3,3));
        //top label
        viewTimer = new Label("00:00:00",Label.CENTER);
        viewTimer.setFont(labelFont);
        add(viewTimer);

        //middle text fields
        Panel panelSetTimer = new Panel(new GridLayout(1,5,3,3));
        setHours = new TextField("00",1);
        setMinutes = new TextField("00",1);
        setSeconds = new TextField("00",1);
        setHours.setFont(textFont);
        setMinutes.setFont(textFont);
        setSeconds.setFont(textFont);
        panelSetTimer.add(setHours);
        panelSetTimer.add(setMinutes);
        panelSetTimer.add(setSeconds);
        add(panelSetTimer);

        //bottom buttons
        Panel panelButtons = new Panel(new GridLayout(1,2,3,3));
        startButton = new Button("Start");
        panelButtons.add(startButton);
        stopButton = new Button("Reset");
        panelButtons.add(stopButton);
        ButtonListener buttonListener = new ButtonListener();
        startButton.addActionListener(buttonListener);
        stopButton.addActionListener(buttonListener);
        add(panelButtons);

        //timer
        TimerListener timerListener = new TimerListener();
        timer = new Timer(1000, timerListener);

        addWindowListener(this);

        setTitle("Countdown Timer");
        setSize(300, 150);
        setVisible(true);
    }

    //listener for the buttons
    private class ButtonListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent evt){
            String buttonLabel = evt.getActionCommand();
            //add the values in text fields to the timer as seconds
            if(buttonLabel.equals("Start")){
                try {
                    timerTime = 0;
                    timerTime += Integer.parseInt(setHours.getText()) * 3600;
                    timerTime += Integer.parseInt(setMinutes.getText()) * 60;
                    timerTime += Integer.parseInt(setSeconds.getText());
                    updateTimerTime();
                    startButton.setLabel("Pause");
                    timer.start();
                }
                catch(NumberFormatException e){
                    viewTimer.setText("Numbers only! >:(");
                }
            }
            //start the timer (again)
            else if(buttonLabel.equals("Continue")){
                startButton.setLabel("Pause");
                timer.start();
            }
            //stop the timer
            else if(buttonLabel.equals("Pause")){
                startButton.setLabel("Continue");
                timer.stop();
            }
            //reset timer to 0 and pause
            else if(buttonLabel.equals("Reset")){
                timerTime = 0;
                updateTimerTime();
                startButton.setLabel("Start");
                timer.stop();
            }
        }
    }

    //listener for the timer, decrement 1 from the timer every second
    private class TimerListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent evt){
            timerTime -= 1;
            //play the sound effect when timer hits 0 and every 2 minutes afterwards
            if(timerTime <= 0 && timerTime % 120 == 0){
                clip.stop();
                clip.setMicrosecondPosition(0);
                clip.start();
            }
            updateTimerTime();
            timer.restart();
        }
    }

    //formats the timer form seconds to a hr:min:sec format
    private void updateTimerTime(){
        String negative = "";
        int absTime = timerTime;
        if(timerTime < 0){
            negative = "-";
            absTime = Math.abs(timerTime);
            viewTimer.setForeground(Color.red);
        }
        else{
            viewTimer.setForeground(Color.black);
        }
        int hours = absTime / 3600;
        int minutes = (absTime - hours*3600) / 60;
        int seconds = absTime - hours*3600 - minutes*60;
        String f = String.format("%s%02d:%02d:%02d", negative, hours, minutes, seconds);
        viewTimer.setText(f);
    }

    //Windowevent handlers
    @Override
    public void windowClosing(WindowEvent evt){
        dispose();
    }

    //Useless, but need to be overridden anyways
    @Override public void windowOpened(WindowEvent evt) { }
    @Override public void windowClosed(WindowEvent evt) { }
    @Override public void windowIconified(WindowEvent evt) {  }
    @Override public void windowDeiconified(WindowEvent evt) {  }
    @Override public void windowActivated(WindowEvent evt) { }
    @Override public void windowDeactivated(WindowEvent evt) {  }

    public static void main(String[] args) {
        new CountdownTimer();
    }
}
