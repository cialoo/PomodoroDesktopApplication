import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame implements ActionListener {

    ImageIcon image;
    private WatchPanel watchPanel;
    private ProgressPanel progressPanel;
    private java.net.URL logoUrl;
    MyFile myFile = new MyFile("pomodoro_progress.txt");

    MainFrame() {

        this.setTitle("Pomodoro");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 500);
        this.setLayout(null);

        logoUrl = getClass().getResource("logoPomodoro.png");
        image = new ImageIcon(logoUrl);
        this.setIconImage(image.getImage());

        watchPanel = new WatchPanel(this);
        progressPanel = new ProgressPanel(this);

        this.setLayout(new CardLayout());

        this.add(watchPanel, "WatchPanel");
        this.add(progressPanel, "ProgressPanel");

        this.setResizable(false);
        this.setVisible(true);;

    }


    public void switchToProgressPanel() {
        CardLayout cardLayout = (CardLayout) getContentPane().getLayout();
        cardLayout.show(getContentPane(), "ProgressPanel");
        if(myFile.doesFileExist()) {
            getProgressPanel().updatelabelTime(myFile.loadProgress());
        }
    }

    public void switchToWatchPanel() {
        CardLayout cardLayout = (CardLayout) getContentPane().getLayout();
        cardLayout.show(getContentPane(), "WatchPanel");
    }

    public WatchPanel getWatchPanel() {
        return watchPanel;
    }

    public ProgressPanel getProgressPanel() {
        return progressPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
