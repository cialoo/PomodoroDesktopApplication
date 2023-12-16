import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;

public class Watch extends JFrame implements ActionListener {

    ImageIcon image;
    JLabel label;
    JButton buttonStart;
    JButton buttonStop;
    JButton buttonRestart;
    JTextField textField;
    long elapsedTime = 0;
    long remainTime = 0;
    long seconds = 0;
    long minutes = 0;
    long hours = 0;
    String secondsStr = String.format("%02d", seconds);
    String minutesStr = String.format("%02d", minutes);
    String hoursStr = String.format("%02d", hours);
    JLabel labelWatch;
    Timer timer = new Timer(1000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(elapsedTime > 0) {
                elapsedTime -= 1000;
                hours = (elapsedTime/3600000);
                minutes = (elapsedTime/60000) % 60;
                seconds = (elapsedTime/1000) % 60;
                secondsStr = String.format("%02d", seconds);
                minutesStr = String.format("%02d", minutes);
                hoursStr = String.format("%02d", hours);
                labelWatch.setText(hoursStr + ":" + minutesStr + ":" + secondsStr);

                if (elapsedTime <= 0) {
                    stop();
                    playRing();
                    completeMessage();
                }
            }
        }
    });

    Clip clip;
    Watch() {

        this.setTitle("Pomodoro");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 500);
        this.setLayout(null);

        image = new ImageIcon("logoPomodoro.png");
        this.setIconImage(image.getImage());

        label = new JLabel();
        label.setBounds(0,0,500,100);
        label.setText("Pomodoro");
        label.setFont(new Font("Consolas", Font.PLAIN, 35));
        label.setForeground(Color.GREEN);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);

        textField = new JTextField("HH:MM:SS");
        textField.setFont(new Font("Consolas", Font.PLAIN, 35));
        textField.setBackground(Color.GREEN);
        textField.setForeground(Color.BLACK);
        textField.setBounds(100,100,300,50);

        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                textField.selectAll();
            }

            @Override
            public void focusLost(FocusEvent e) {

            }

        });

        labelWatch = new JLabel();
        labelWatch.setText(hoursStr + ":" + minutesStr + ":" + secondsStr);
        labelWatch.setBounds(100, 200, 300,100);
        labelWatch.setFont(new Font("Consolas", Font.PLAIN, 35));
        labelWatch.setForeground(Color.GREEN);
        labelWatch.setHorizontalAlignment(JLabel.CENTER);
        labelWatch.setVerticalAlignment(JLabel.CENTER);

        buttonStart = new JButton();
        buttonStart.addActionListener(this);
        buttonStart.setText("Start");
        buttonStart.setBounds(200, 350, 100,25);

        buttonStop = new JButton();
        buttonStop.addActionListener(this);
        buttonStop.setText("Stop");
        buttonStop.setBounds(200, 375, 100,25);

        buttonRestart = new JButton();
        buttonRestart.addActionListener(this);
        buttonRestart.setText("Restart");
        buttonRestart.setBounds(200, 400, 100,25);

        this.add(buttonRestart);
        this.add(textField);
        this.add(labelWatch);
        this.add(label);
        this.add(buttonStart);
        this.add(buttonStop);
        this.getContentPane().setBackground(Color.BLACK);
        this.setResizable(false);
        this.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==buttonStart) {
            if(elapsedTime == remainTime || elapsedTime <= 0) {
                set();
            }
            start();
        } else if (e.getSource()==buttonStop) {
            stop();
        } else if (e.getSource()==buttonRestart) {
            restart();
        }
    }

    void start() {

        timer.start();

    }

    void stop() {

        timer.stop();

    }

    void restart() {

        stop();
        elapsedTime = remainTime;
        start();

    }

    void set() {

        try {
            String[] timeComponents = textField.getText().split(":");
            if (timeComponents.length == 3) {
                hours = Integer.parseInt(timeComponents[0].trim());
                minutes = Integer.parseInt(timeComponents[1].trim());
                seconds = Integer.parseInt(timeComponents[2].trim());

                elapsedTime = hours * 3600000 + minutes * 60000 + seconds * 1000;

                remainTime = elapsedTime;

            } else {
                errorMessage();
            }
        } catch (NumberFormatException ex) {
            errorMessage();
        }

    }

    void playRing() {
        try {
            File soundFile = new File("ring.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    void stopRing() {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }

    void playError() {
        try {
            File soundFile = new File("error.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            clip = AudioSystem.getClip();
            clip.open(audioIn);
            long startPosition = 1000000;
            clip.setMicrosecondPosition(startPosition);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    void completeMessage() {
        JOptionPane.showMessageDialog(null, "Completed!", "Pomodoro", JOptionPane.INFORMATION_MESSAGE);
        stopRing();
    }

    void errorMessage() {
        playError();
        JOptionPane.showMessageDialog(null, "Enter the correct time (HH:MM:SS)!", "Pomodoro",JOptionPane.WARNING_MESSAGE);
    }

}