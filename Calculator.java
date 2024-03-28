import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;
import javax.swing.JOptionPane;

class Calculator{
    public static void main(String[] args){
        MyWindow mw = new MyWindow();
    }
}

class MyWindow extends JFrame implements ActionListener{
    JTextArea ta, sta;
    JMenuItem mopen, msave, owsave, mcut, mcopy, mpaste, msearch;
    JButton btn;
    JFrame searchFrame;
    String di, fi;
    boolean flag;
    MyWindow(){
        setTitle("テキストエディタ");
        setSize(800, 600);
        ta = new JTextArea();
        add(ta);
        ta.getDocument().addDocumentListener(new DocumentListener(){
            public void insertUpdate(DocumentEvent e) {
                flag = true;
            }
            public void removeUpdate(DocumentEvent e) {
                flag = true;
            }
            public void changedUpdate(DocumentEvent e) {
                flag = true;
            }
            });
        JMenuBar mbar = new JMenuBar();
        JMenu menu = new JMenu("ファイル");
        JMenu menu2 = new JMenu("編集");
        mopen = new JMenuItem("開く");
        msave = new JMenuItem("保存");
        owsave = new JMenuItem("上書き保存");
        mcut = new JMenuItem("切り取り");
        mcopy = new JMenuItem("コピー");
        mpaste = new JMenuItem("貼り付け");
        msearch = new JMenuItem("検索");
        mopen.addActionListener(this);
        msave.addActionListener(this);
        owsave.addActionListener(this);
        mcut.addActionListener(this);
        mcopy.addActionListener(this);
        mpaste.addActionListener(this);
        msearch.addActionListener(this);
        menu.add(mopen);
        menu.add(msave);
        menu.add(owsave);
        menu2.add(mcut);
        menu2.add(mcopy);
        menu2.add(mpaste);
        menu2.add(msearch);
        mbar.add(menu);
        mbar.add(menu2);
        setJMenuBar(mbar);
        setVisible(true);
        addWindowListener(new WinListener());
        flag = false;
    }

    public void actionPerformed(ActionEvent e){
        if (e.getSource() == mopen) loadCheck();
        if (e.getSource() == msave) saveFile();
        if (e.getSource() == owsave) owFile();
        if (e.getSource() == mcut) ta.cut();
        if (e.getSource() == mcopy) ta.copy();
        if (e.getSource() == mpaste) ta.paste();
        if (e.getSource() == msearch) searchFrame();
        if (e.getSource() == btn) search();
    }

    void loadCheck(){
        if(flag){
                int log = JOptionPane.showConfirmDialog(MyWindow.this, "保存せずに開きますか？", "注意！",  JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if(log == JOptionPane.YES_OPTION){
                    loadFile();
                }
                else {
                    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
                }
            }
            else{
                loadFile();
            }
    }

    void loadFile(){
        FileDialog fd = new FileDialog(this, "Load", FileDialog.LOAD);
        fd.setVisible(true);
        di = fd.getDirectory();
        fi = fd.getFile();
        System.out.println("ディレクトリ:" + di);
        System.out.println("ファイル名:" + fi);
        if(fi != null){
            String kaigyo = System.getProperty("line.separator");
            FileReader fr;
            BufferedReader br;
            try{
                fr = new FileReader(di + fi);
                br = new BufferedReader(fr);
                ta.setText("");
                while(true){
                    String str = br.readLine();
                    if(str == null) break;
                    ta.setText(ta.getText() + str + kaigyo);
                }
                br.close();
                fr.close();
                flag = false;
            }
            catch(IOException e){
                System.out.println("ファイルが読み込めません");
            }
        }
    }

    void saveFile(){
        FileDialog fd = new FileDialog(this, "Save", FileDialog.SAVE);
        fd.setVisible(true);
        di = fd.getDirectory();
        fi = fd.getFile();
        System.out.println("ディレクトリ:" + di);
        System.out.println("ファイル名:" + fi);
        if(fi != null){
            try{
                FileWriter fw = new FileWriter(di + fi);
                fw.write( ta.getText() );
                fw.close();
                flag = false;
            }
            catch(IOException e){
                System.out.println("ファイルが書き込めません");
            }
        }
    }

    void owFile(){
        if(fi != null){
            try{
                FileWriter fw = new FileWriter(di + fi);
                fw.write( ta.getText() );
                fw.close();
                flag = false;
            }
            catch(IOException e){
                System.out.println("ファイルが書き込めません");
            }
        }
    }

    void searchFrame(){
        searchFrame = new JFrame("検索欄");
        sta = new JTextArea();
        btn = new JButton("検索");
        searchFrame.setSize(600, 100);
        searchFrame.setLayout(new BorderLayout());
        btn.addActionListener(this);
        searchFrame.add(sta);
        searchFrame.add(btn, BorderLayout.SOUTH);
        searchFrame.setVisible(true);  
    }

    void search(){
        String str1 = ta.getText();
        String str2 = sta.getText();
        Highlighter line = ta.getHighlighter();
        line.removeAllHighlights();
        int StartIndex = 0;
        DefaultHighlightPainter painter = new DefaultHighlightPainter(Color.YELLOW);
        while((StartIndex = str1.indexOf(str2, StartIndex)) != -1){
            int EndIndex = StartIndex + str2.length();
            try{
                line.addHighlight(StartIndex, EndIndex, painter);
                StartIndex = EndIndex;
            } catch(BadLocationException e){
                JOptionPane.showMessageDialog(null, "見つかりませんでした");
            }
        }
        if(line.getHighlights().length == 0){
                JOptionPane.showMessageDialog(null, "見つかりませんでした");
            }
        searchFrame.dispose();
    }

    class WinListener extends WindowAdapter{
        public void windowClosing(WindowEvent e){
            if(flag){
                int log = JOptionPane.showConfirmDialog(MyWindow.this, "保存せずに終了しますか？", "注意！",  JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if(log == JOptionPane.YES_OPTION){
                    System.exit(0);
                }
                else {
                    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
                }
            }
            else{
                System.exit(0);
            }
            
        }
    }
}

