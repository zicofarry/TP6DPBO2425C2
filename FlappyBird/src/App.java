import javax.swing.*;

public class App {
    static void main(String[] args){
        JFrame frame = new JFrame("Flappy Bird");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(360, 640);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
//        frame.setVisible(true);

        Logic logic = new Logic(); // instalasi logic
        // instalasi sehingga view bisa berkomunikasi dengan logic
        View view = new View(logic);
        // begitu pula kebalikannya
        logic.setView(view);

        // window View menerima fokus
        view.requestFocus();

        // instalasi objek view
        frame.add(view);
        frame.pack();
        frame.setVisible(true);
    }
}
