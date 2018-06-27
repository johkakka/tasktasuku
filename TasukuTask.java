import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import javax.swing.*;

class TasukuTask{
	static String VAR = "TasukuTask ver.1.2  by Amada.";
	
	
	
	static ArrayList<Task> tasks = new ArrayList<Task>();
	static ArrayList<LimitedTask> limitedTasks = new ArrayList<LimitedTask>();
	static boolean isSaved = true;
	static boolean isLimitedTask = false;
	static File log = new File("log.txt");
	
	private static final DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy/MM/dd(HH:mm:ss)");
	
	private String homeText = "<html>" + VAR + "<br>";
	private String taskText = "<html>";
	
	private JLabel taskLabel,deleteLabel;
	
	int x,y;
	
	private JButton homeB,addB,taskB,deleteB,saveB;
	private JPanel homeCard,addCard,taskCard,deleteCard;
	
	private JLabel calendar;
	
	private CardLayout mainCard;
	private JPanel mainContainer;
	private Container mainContainer2;
	
	JSpinner yearS,monthS,dayS,hourS,minuteS;
	
	private JTextField nameField,subjectField;
	
	private JSpinner delNo;
	
	public void visualSetup(TasukuTask tasukuTask){
		Timer timer = new Timer(1000,new ClockText());
		timer.start();
		
		//画面解像度の取得
		Rectangle desktopBounds;
		GraphicsEnvironment env; 
		Dimension maxSize; 
		env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		desktopBounds = env.getMaximumWindowBounds();
		maxSize = desktopBounds.getSize();
		//ウィンドウサイズの最適化
		x = maxSize.width;
		y = maxSize.height;
		int frX = x*7/10;
		int frY = y*3/5;
		
		Dimension frameSize = new Dimension(frX,frY); 

		//よく使う色
		Color theme = new Color(135, 148, 0);
		
		//メインフレーム
		JFrame frame = new JFrame("タスク管理ソフト - TasukuTask");
		frame.setSize(frameSize);
		frame.setLocation(x*1/5, y*1/5);
		frame.addWindowListener(new ExitAction());
		
		frame.setLayout(new BorderLayout());
		
		
		//左メニューパネル部描画
		JPanel menuPanel = new JPanel();
		menuPanel.setPreferredSize(new Dimension(frX/5, frY));
		menuPanel.setBackground(theme);
		menuPanel.setLayout(new GridLayout(6, 1));
		frame.add(menuPanel,BorderLayout.WEST);
		
			//タイトル表示
			JLabel title = new JLabel("TasukuTask");
			title.setHorizontalAlignment(JLabel.CENTER);
			title.setFont(new Font(null, Font.BOLD|Font.ITALIC, 40));
			title.setBackground(theme);
			title.setForeground(Color.white);
			menuPanel.add(title);
			
			//メニューボタン
			Font bottonF = new Font(null, Font.PLAIN, 30);
			
			homeB = new JButton("Home");
			homeB.setFont(bottonF);
			homeB.addActionListener(tasukuTask.new ButtonAciton());
			menuPanel.add(homeB);
			
			taskB = new JButton("Task");
			taskB.setFont(bottonF);
			taskB.addActionListener(tasukuTask.new ButtonAciton());
			menuPanel.add(taskB);
			
			addB = new JButton("Add");
			addB.setFont(bottonF);
			addB.addActionListener(tasukuTask.new ButtonAciton());
			menuPanel.add(addB);
			
			deleteB = new JButton("Delete");
			deleteB.setFont(bottonF);
			deleteB.addActionListener(tasukuTask.new ButtonAciton());
			menuPanel.add(deleteB);
			
			saveB = new JButton("Save");
			saveB.setFont(bottonF);
			saveB.addActionListener(tasukuTask.new ButtonAciton());
			saveB.setEnabled(!isSaved);
			menuPanel.add(saveB);
			
		//右メイン画面描画
		mainContainer = new JPanel();
		frame.add(mainContainer, BorderLayout.CENTER);
		mainContainer.setLayout(new BorderLayout());
		JPanel upMargin = new JPanel();
		upMargin.setPreferredSize(new Dimension(frX*2/3, 30));
		mainContainer.add(upMargin, BorderLayout.NORTH);
		JPanel downMargin = new JPanel();
		downMargin.setPreferredSize(new Dimension(frX*2/3, 30));
		mainContainer.add(downMargin, BorderLayout.SOUTH);
		JPanel rightMargin = new JPanel();
		rightMargin.setPreferredSize(new Dimension(30, frY));
		mainContainer.add(rightMargin, BorderLayout.EAST);
		JPanel leftMargin = new JPanel();
		leftMargin.setPreferredSize(new Dimension(30, frY));
		mainContainer.add(leftMargin, BorderLayout.WEST);
		
		mainCard = new CardLayout();
		mainContainer2 = new Container();
		mainContainer2.setLayout(mainCard);
		mainContainer.add(mainContainer2, BorderLayout.CENTER);
		
			homeCard  = new JPanel();
			homeCard.setLayout(new BorderLayout());
			mainContainer2.add(homeCard, "Home");
			
				JLabel homeTitle = new JLabel("TasukuTask");
				homeTitle.setFont(new Font(null, Font.BOLD|Font.ITALIC, 60));
				homeTitle.setHorizontalAlignment(JLabel.CENTER);
				homeTitle.setForeground(theme);
				homeCard.add(homeTitle, BorderLayout.NORTH);
				
				JPanel homeCenter = new JPanel();
				homeCenter.setLayout(new BorderLayout());
				homeCard.add(homeCenter,BorderLayout.CENTER);
				
				calendar = new JLabel("只今,"+LocalDateTime.now().format(f)+"です");
				calendar.setFont(new Font(null, Font.PLAIN, 40));
				calendar.setHorizontalAlignment(JLabel.CENTER);
				homeCenter.add(calendar, BorderLayout.NORTH);
				
				JLabel homeLabel = new JLabel(homeText+"</html>");
				homeLabel.setFont(new Font(null, Font.PLAIN, 20));
				homeLabel.setVerticalAlignment(JLabel.TOP);
				JScrollPane homeMain = new JScrollPane(homeLabel);
				homeCenter.add(homeMain);
				
			taskCard = new JPanel();
			taskCard.setLayout(new BorderLayout());
				JLabel taskTitle = new JLabel("現在のtask");
				taskTitle.setFont(new Font(null, Font.BOLD, 40));
				taskCard.add(taskTitle,BorderLayout.NORTH);
				taskLabel = new JLabel(taskText+outputTasks()+"</html>");
				taskLabel.setFont(new Font(null, Font.PLAIN, 20));
				taskLabel.setVerticalAlignment(JLabel.TOP);
				JScrollPane taskMain = new JScrollPane(taskLabel);
				taskCard.add(taskMain,BorderLayout.CENTER);
			mainContainer2.add(taskCard, "Task");
			
			addCard = new JPanel();
			mainContainer2.add(addCard, "Add");
			addCard.setLayout(new GridLayout(6, 1,0,30));
			
				JLabel addTitle = new JLabel("taskの新規追加");
				addTitle.setFont(new Font(null, Font.BOLD, 40));
				addCard.add(addTitle);
				
				Container limitRadioContiner = new Container();
				ButtonGroup limitBG = new ButtonGroup();
				limitRadioContiner.setLayout(new GridLayout(1, 3));
					JLabel buttonLebel = new JLabel("期限付きtaskですか?");
					buttonLebel.setFont(new Font(null, Font.PLAIN, 20));
					limitRadioContiner.add(buttonLebel);
					
					JRadioButton limitRadioB = new JRadioButton("はい");
					limitRadioB.setFont(new Font(null, Font.PLAIN, 20));
					limitRadioContiner.add(limitRadioB);
					limitBG.add(limitRadioB);
					limitRadioB.addActionListener(tasukuTask.new LimitTimeSetingAction());
					
					
					JRadioButton notLimitRadioB = new JRadioButton("いいえ",true);
					notLimitRadioB.setFont(new Font(null, Font.PLAIN, 20));
					limitRadioContiner.add(notLimitRadioB);
					limitBG.add(notLimitRadioB);
					notLimitRadioB.addActionListener(tasukuTask.new LimitTimeSetingAction());
				
				addCard.add(limitRadioContiner);
				
				JPanel limitTimePanel = new JPanel();
				limitTimePanel.setLayout(new GridLayout(1, 12));
				addCard.add(limitTimePanel);
					JLabel limitTimeRabel = new JLabel("期限");
					limitTimeRabel.setFont(new Font(null, Font.PLAIN, 20));
					limitTimePanel.add(limitTimeRabel);
						
					JPanel tmp1 = new JPanel();
					limitTimePanel.add(tmp1);
					
					yearS = new JSpinner(new SpinnerNumberModel(LocalDateTime.now().getYear(), 1900, 2199, 1));
					yearS.setEnabled(false);
					yearS.setPreferredSize(new Dimension(100, 30));
					yearS.setFont(new Font(null, Font.PLAIN, 20));
					JSpinner.NumberEditor editorY = new JSpinner.NumberEditor(yearS, "####");
					yearS.setEditor(editorY);
					limitTimePanel.add(yearS);
					
					JLabel yearL = new JLabel("年");
					yearL.setFont(new Font(null, Font.PLAIN, 20));
					limitTimePanel.add(yearL);
					
					monthS = new JSpinner(new SpinnerNumberModel(LocalDateTime.now().getMonthValue(), 1, 12, 1));
					monthS.setEnabled(false);
					monthS.setPreferredSize(new Dimension(50, 30));
					monthS.setFont(new Font(null, Font.PLAIN, 20));
					limitTimePanel.add(monthS);
					
					JLabel monthL = new JLabel("月");
					monthL.setFont(new Font(null, Font.PLAIN, 20));
					limitTimePanel.add(monthL);
				
					dayS = new JSpinner(new SpinnerNumberModel(LocalDateTime.now().getDayOfMonth(), 1, 31, 1));
					dayS.setEnabled(false);
					dayS.setPreferredSize(new Dimension(50, 30));
					dayS.setFont(new Font(null, Font.PLAIN, 20));
					limitTimePanel.add(dayS);
					
					JLabel dayL = new JLabel("日");
					dayL.setFont(new Font(null, Font.PLAIN, 20));
					limitTimePanel.add(dayL);
					
					hourS = new JSpinner(new SpinnerNumberModel(LocalDateTime.now().getHour(), 0, 23, 1));
					hourS.setEnabled(false);
					hourS.setPreferredSize(new Dimension(50, 30));
					hourS.setFont(new Font(null, Font.PLAIN, 20));
					limitTimePanel.add(hourS);
					
					JLabel hourL = new JLabel("時");
					hourL.setFont(new Font(null, Font.PLAIN, 20));
					limitTimePanel.add(hourL);
					
					minuteS = new JSpinner(new SpinnerNumberModel(LocalDateTime.now().getMinute(), 0, 59, 1));
					minuteS.setEnabled(false);
					minuteS.setPreferredSize(new Dimension(50, 30));
					minuteS.setFont(new Font(null, Font.PLAIN, 20));
					limitTimePanel.add(minuteS);
					
					JLabel minuteL = new JLabel("分");
					minuteL.setFont(new Font(null, Font.PLAIN, 20));
					limitTimePanel.add(minuteL);
					
				JPanel namePanel = new JPanel();
				namePanel.setLayout(new GridLayout(1,4));
				addCard.add(namePanel);
					JLabel nameL = new JLabel("task名");
					nameL.setFont(new Font(null, Font.PLAIN, 20));
					namePanel.add(nameL);
					
					nameField = new JTextField();
					nameField.setFont(new Font(null, Font.PLAIN, 20));
					nameField.setPreferredSize(new Dimension(150, 30));
					namePanel.add(nameField);
					
					JPanel tmp2 = new JPanel();
					namePanel.add(tmp2);
	
					JPanel tmp3 = new JPanel();
					namePanel.add(tmp3);
					
				JPanel subjectPanel = new JPanel();
				subjectPanel.setLayout(new GridLayout(1,4));
				addCard.add(subjectPanel);
					JLabel subjectL = new JLabel("科目(分類)名");
					subjectL.setFont(new Font(null, Font.PLAIN, 20));
					subjectPanel.add(subjectL);
					
					
					subjectField = new JTextField();
					subjectField.setFont(new Font(null, Font.PLAIN, 20));
					subjectField.setPreferredSize(new Dimension(150, 30));
					subjectPanel.add(subjectField);
					
					
					JPanel tmp4 = new JPanel();
					subjectPanel.add(tmp4);
	
					JPanel tmp5 = new JPanel();
					subjectPanel.add(tmp5);
	
				JButton addEnterB = new JButton("Enter");
				addEnterB.setFont(new Font(null, Font.PLAIN, 20));
				addCard.add(addEnterB);
				addEnterB.addActionListener(new ButtonAciton());
				addEnterB.addActionListener(new AddAction());	
			
			
			deleteCard = new JPanel();
			mainContainer2.add(deleteCard, "Delete");
			deleteCard.setLayout(new BorderLayout());
			
				JLabel deleteTitle = new JLabel("taskの削除");
				deleteTitle.setFont(new Font(null, Font.BOLD, 40));
				deleteCard.add(deleteTitle,BorderLayout.NORTH);
				
				deleteLabel = new JLabel(taskText+outputTasks()+"</html>");
				deleteLabel.setFont(new Font(null, Font.PLAIN, 20));
				deleteLabel.setVerticalAlignment(JLabel.TOP);
				JScrollPane deleteMain = new JScrollPane(deleteLabel);
				deleteCard.add(deleteMain,BorderLayout.CENTER);
				
				JPanel deleteDown = new JPanel();
				deleteDown.setPreferredSize(new Dimension(frX*2/3, 100));
				deleteCard.add(deleteDown,BorderLayout.SOUTH);
				deleteDown.setLayout(new GridLayout(1, 4,30,0));
				
					JLabel deleteDownTitle = new JLabel("削除対象No.");
					deleteDownTitle.setFont(new Font(null, Font.PLAIN, 20));
					deleteDown.add(deleteDownTitle);
					
					delNo = new JSpinner();
					if(tasks.size()+limitedTasks.size() > 0){
					delNo = new JSpinner(new SpinnerNumberModel(0, 0,tasks.size()+limitedTasks.size()-1, 1));
					}else{
						delNo.setEnabled(false);
					}
					delNo.setFont(new Font(null, Font.PLAIN, 20));
					deleteDown.add(delNo);
					
					JButton delEnter = new JButton("Delete Task");
					delEnter.setFont(new Font(null, Font.PLAIN, 20));
					deleteDown.add(delEnter);
					delEnter.addActionListener(new ButtonAciton());
					delEnter.addActionListener(new DelAction());
					
					JPanel tem6 = new JPanel();
					deleteDown.add(tem6);
			
		frame.setVisible(true);
	}
	
	public static void main(String args[]){
		TasukuTask tasukuTask = new TasukuTask();
		tasukuTask.setup(tasukuTask);
		tasukuTask.visualSetup(tasukuTask);
		
		System.out.println("プログラムは起動しました");
		
	}

	@SuppressWarnings("unchecked")
	public void setup(TasukuTask tasukuTask){
		
		System.out.println("#####################");
		System.out.println("##   Tasku Task    ##");
		System.out.println("##  author:Amada   ##");
		System.out.println("##  ID:26001700145 ##");
		System.out.println("#####################");
		
		try{
			Scanner logScan = new Scanner(log);
			String date;
			String dates[];
			//int i = 0;
			
			while(logScan.hasNextLine()){
				//System.out.println(i);
				date = logScan.nextLine();
				dates = date.split(",");
				if(dates.length == 3){
					Task task = new Task();
					task.setName(dates[0]);
					task.setSubject(dates[1]);
					task.setTime(LocalDateTime.parse(dates[2]));
					tasks.add(task);
				}else if(dates.length == 4){
					LimitedTask limitedTask = new LimitedTask();
					limitedTask.setName(dates[0]);
					limitedTask.setSubject(dates[1]);
					limitedTask.setTime(LocalDateTime.parse(dates[2]));
					limitedTask.setLimitTime(LocalDateTime.parse(dates[3]));
					limitedTasks.add(limitedTask);
				}else{
//					System.out.println("壊れたlogを検出しました");
					homeText += "壊れたlogを検出しました<br>";
				}
				//i++;
			}
			
			logScan.close();
		}catch (IOException ie){
//			System.out.println("logファイルが見つかりません");
			homeText += "logファイルが見つかりません<br>";
			PrintWriter printWriterStart = null;
			try {
				printWriterStart = new PrintWriter(new BufferedWriter(new FileWriter(log)));
			} catch (IOException e) {
			}
			printWriterStart.close();
		}
		
		
		
		Collections.sort(limitedTasks,new LimitedTaskComparator());
		
		boolean isFirst = true;
		boolean isFirst2 = true;
		for(LimitedTask limitedTask:limitedTasks){
			if(limitedTask.getRestDay() < 0){
				if(isFirst){
//					System.out.println();
//					System.out.println("**期限が過ぎたtaskがあります**");
					homeText += "<br>**期限が過ぎたtaskがあります**<br>";
					isFirst = false;
				}
				homeText += limitedTask+"<br>";
//				System.out.println(limitedTask);
				
			}else if(limitedTask.getRestDay() < 8){
				if(isFirst2){
//					System.out.println();
//					System.out.println("**期限が近づいているtaskがあります**");
					homeText += "<br>**期限が近づいているtaskがあります**<br>";
				}
//				System.out.println(limitedTask);
				homeText += limitedTask+"<br>";
			}
		}
	}

	
	static void add(String name,String subject){
		isSaved = false;
		Task task = new Task();
		if(name != null&&!name.equals("")){
			task.setName(name);
		}
		if(subject!= null&&!subject.equals("")){
			task.setSubject(subject);
		}
		tasks.add(task);
	}
	
	@SuppressWarnings("unchecked")
	static void add(String name,String subject,LocalDateTime localDateTime){
		isSaved = false;
		LimitedTask limitedTask = new LimitedTask();
		if(name != null&&!name.equals("")){
			limitedTask.setName(name);
		}
		if(subject!= null&&!subject.equals("")){
			limitedTask.setSubject(subject);
		}
		limitedTask.setLimitTime(localDateTime);
		limitedTasks.add(limitedTask);
		Collections.sort(limitedTasks,new LimitedTaskComparator());

	}
	
	static void del(int num){
		isSaved = false;
		if(num < limitedTasks.size()){
			limitedTasks.remove(num);
		}else{
			tasks.remove(num - limitedTasks.size());
		}
		
	}
	
	static void save(){
		try{
			PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(log)));
			
			for(LimitedTask limitedTask:limitedTasks){
				 printWriter.println(limitedTask.getName()+","+
						 		     limitedTask.getSubject()+","+
						 		     limitedTask.getTime()+","+
						 		     limitedTask.getLimitTime());
			 }
			
			for(Task task:tasks){
				 printWriter.println(task.getName()+","+
						 		     task.getSubject()+","+
						 		     task.getTime());
			 }
			
			 isSaved = true;
			 printWriter.close();
			 
			 System.out.println("変更を保存しました");
		}catch(IOException ioException){
			System.out.println("log処理に失敗しました");
			System.out.println(ioException);
		}
	}
	
	static String outputTasks(){
		String result = "";
		if(tasks.isEmpty()&&limitedTasks.isEmpty()){
			result += "no task<br>";
		}else{
			int i = 0;
			result += "N  The time when task made: name(subject)<br>";
			result += "--------------------------------------<br>";
			
			for(LimitedTask limitedTask:limitedTasks){
				if(limitedTask.getRestDay() < 0){
					result += "<font color=e74c3c>";
				}else if(limitedTask.getRestDay() <8){
					result += "<font color=ff8822>";
				}else{
					result += "<font color=3498db>";
				}
				result += i+" "+limitedTask+"</font><br>";
				i++;
			}

			for(Task task:tasks){
				result += i+" "+task+"<br>";
				i++;
			}
		}
		
	
		return result;
	}
	
	public void popup(String title,String message){
		JFrame popupWindow = new JFrame(title);
		popupWindow.setBounds(x/3, y/3, 400, 150);
		popupWindow.setLayout(new BorderLayout());
		JLabel text = new JLabel(message);
		text.setFont(new Font(null, Font.PLAIN, 20));
		text.setHorizontalAlignment(JLabel.CENTER);
		popupWindow.add(text,BorderLayout.CENTER);
		
		popupWindow.setVisible(true);
	}
	
	public class ButtonAciton implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			switch(command){
				case "Home":
					homeB.setEnabled(false);
					addB.setEnabled(true);
					deleteB.setEnabled(true);
					saveB.setEnabled(!isSaved);
					taskB.setEnabled(true);
					
					mainCard.show(mainContainer2, "Home");
					
					break;
				case "Add":
					homeB.setEnabled(true);
					addB.setEnabled(false);
					deleteB.setEnabled(true);
					saveB.setEnabled(!isSaved);
					taskB.setEnabled(true);
					
					mainCard.show(mainContainer2, "Add");
					
					break;
				case "Delete":
					homeB.setEnabled(true);
					addB.setEnabled(true);
					deleteB.setEnabled(false);
					saveB.setEnabled(!isSaved);
					taskB.setEnabled(true);
					
					deleteLabel.setText(taskText+outputTasks()+"</html>");
					mainCard.show(mainContainer2, "Delete");
					
					break;
				case "Save":
					homeB.setEnabled(true);
					addB.setEnabled(true);
					deleteB.setEnabled(true);
					saveB.setEnabled(!isSaved);
					taskB.setEnabled(true);
					
					save();
					saveB.setEnabled(!isSaved);
					
					break;
				case "Task":
					homeB.setEnabled(true);
					addB.setEnabled(true);
					deleteB.setEnabled(true);
					saveB.setEnabled(!isSaved);
					taskB.setEnabled(false);
					
					taskLabel.setText(taskText+outputTasks()+"</html>");
					mainCard.show(mainContainer2, "Task");
					break;
			
				case "Enter":
				case "Delete Task":
					saveB.setEnabled(!isSaved);
					break;
					
				case "保存して閉じる":
					save();
				case "保存せず閉じる":
					System.exit(0);
					break;
			}
		
		}
		
		
	}
	
	public class ClockText implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			calendar.setText("只今,"+LocalDateTime.now().format(f)+"です");
		}
		
	}
	
	public class AddAction implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(isLimitedTask){
				LocalDateTime localDateTime = null;
				 
				String  yearStr = "1900";
				String monthStr = "01";
				String dayStr = "01";
				String hourStr = "00";
				String minuteStr = "00";
				
				try{
					yearStr = yearS.getValue().toString();
					
					if((int)yearS.getValue() < 10){
						yearStr = "0"+yearStr;
					}
					
					monthStr = monthS.getValue().toString();
					if((int)monthS.getValue() < 10){
						monthStr = "0"+monthStr;
					}
					
					dayStr = dayS.getValue().toString();
					if((int)dayS.getValue() < 10){
						dayStr = "0"+dayStr;
					}
					
					hourStr = hourS.getValue().toString();
					if((int)hourS.getValue() < 10){
						hourStr = "0"+hourStr;
					}
					minuteStr = minuteS.getValue().toString();
					if((int)minuteS.getValue() < 10){
						minuteStr = "0"+minuteStr;
					}
				}catch(ClassCastException castException){
					System.out.println("日時が識別できませんでした");
					System.out.println(castException);
					popup("エラー","日時が識別できませんでした");
				}
				
				try{
					localDateTime = LocalDateTime.parse(yearStr+"/"+
														monthStr+
														"/"+dayStr+
														"("+hourStr+
														":"+minuteStr
														+":00)", f);
					add(nameField.getText(), subjectField.getText(),localDateTime);
				}catch(DateTimeParseException dateTimeParseException){
					System.out.println("日時が不正な型です");
					popup("エラー","日時が不正な型です");
				}
			}else{
				add(nameField.getText(), subjectField.getText());
			}
			nameField.setText("");
			subjectField.setText("");
			
			if(tasks.size()+limitedTasks.size() > 0){
				delNo.setModel(new SpinnerNumberModel(0, 0,tasks.size()+limitedTasks.size()-1, 1));
				delNo.setEnabled(true);
			}else{
				delNo.setEnabled(false);
			}
			
			popup("task追加","新しくtaskを追加しました");
		}
		
	}
	
	public class DelAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			try{
				del((int)delNo.getValue());
				popup("task削除","taskを削除しました");
				
			}catch(Exception exception){
				System.out.println("値が不正です");
				popup("エラー","値が不正です");
				
			}
			if(tasks.size()+limitedTasks.size() > 0){
				delNo.setModel(new SpinnerNumberModel(0, 0,tasks.size()+limitedTasks.size()-1, 1));
			}else{
				delNo.setEnabled(false);
			}
			delNo.setEnabled(true);
			deleteLabel.setText(taskText+outputTasks()+"</html>");
			
		}
	}
	
	public class LimitTimeSetingAction implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			boolean result = false;
			if(command.equals("はい")){
				result = true;
			}
			
			yearS.setEnabled(result);
			monthS.setEnabled(result);
			dayS.setEnabled(result);
			hourS.setEnabled(result);
			minuteS.setEnabled(result);
			isLimitedTask = result;
			
		}
		
	}
	
	public class ExitAction implements WindowListener{
		@Override
		public void windowOpened(java.awt.event.WindowEvent e) {
		}

		@Override
		public void windowClosing(java.awt.event.WindowEvent e) {			
			if(!isSaved){
				JFrame popupWindow = new JFrame("注意");
				popupWindow.setBounds(x/3, y/3, 400, 150);
				popupWindow.setLayout(new BorderLayout());
				JLabel text = new JLabel("<html>変更が保存されていません．<br>保存しますか</html>");
				text.setFont(new Font(null, Font.PLAIN, 20));
				text.setHorizontalAlignment(JLabel.CENTER);
				popupWindow.add(text,BorderLayout.CENTER);
				JPanel down = new JPanel();
				down.setLayout(new GridLayout(1, 2));
				popupWindow.add(down, BorderLayout.SOUTH);
				JButton yes = new JButton("保存して閉じる");
				yes.setFont(new Font(null, Font.PLAIN, 20));
				down.add(yes);
				yes.addActionListener(new ButtonAciton());
				JButton no = new JButton("保存せず閉じる");
				no.setFont(new Font(null, Font.PLAIN, 20));
				down.add(no);
				no.addActionListener(new ButtonAciton());
				
				popupWindow.setVisible(true);
			}else{
				System.exit(0);
			}
		}

		@Override
		public void windowClosed(java.awt.event.WindowEvent e) {			
		}

		@Override
		public void windowIconified(java.awt.event.WindowEvent e) {	
		}

		@Override
		public void windowDeiconified(java.awt.event.WindowEvent e) {
		}

		@Override
		public void windowActivated(java.awt.event.WindowEvent e) {
		}

		@Override
		public void windowDeactivated(java.awt.event.WindowEvent e) {
		}
	}
	
}

class Task{
	String title;		//タスク名
	String subject;		//科目
	LocalDateTime madeTime;//作成時刻
	
	
	private static final DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy/MM/dd(HH:mm:ss)");
	
	public Task() {
		title = "no title"; //タスク名
		subject = "other";	//科目
		
		madeTime = LocalDateTime.now();	//作成時刻
	}
	
	public void setName(String string){
		this.title = string;
	}
	
	public void setTime(LocalDateTime localDateTime){
		this.madeTime = localDateTime;
	}
	
	public void setSubject(String string){
		this.subject = string;
	}
	
	public String getName(){
		return this.title;
	}
	
	public String getSubject(){
		return this.subject;
	}
	
	public LocalDateTime getTime(){
		return this.madeTime;
	}
	
	public String toString(){
		return this.madeTime.format(f)+": "+this.title+"("+this.subject+")";
	}
}

class LimitedTask extends Task{
	LocalDateTime limitTime;
	private static final DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy/MM/dd(HH:mm:ss)");
	
	public LimitedTask() {
		super();
		limitTime = null; //締め切り時刻
	}
	
	public void setLimitTime(LocalDateTime limitTime) {
		this.limitTime = limitTime;
	}
	
	public LocalDateTime getLimitTime() {
		return limitTime;
	}
	
	public long getRestDay(){
		Duration duration = Duration.between(LocalDateTime.now(),this.getLimitTime());
		long result = duration.getSeconds() < 0 && duration.toDays() == 0? -1: duration.toDays();
		return result;
	}
	
	public String toString(){
		return super.madeTime.format(f)+": "+super.title+"("+super.subject+") "+"[until:"+this.limitTime.format(f)+" rest "+this.getRestDay()+"days]";
	}
}

@SuppressWarnings("rawtypes")
class LimitedTaskComparator implements java.util.Comparator{
	public int compare(Object a,Object b){
		return (int) (((LimitedTask)a).getRestDay() - ((LimitedTask)b).getRestDay());
	}
}