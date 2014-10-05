package net.sf.okapi.acorn.client;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.ws.rs.core.Response;

import net.sf.okapi.acorn.taus.TransAPIClient;
import net.sf.okapi.acorn.xom.Factory;

import org.oasisopen.xliff.om.v1.ISegment;
import org.oasisopen.xliff.om.v1.IXLIFFFactory;

public class TausAPIPanel extends JPanel {

	private final static long serialVersionUID = 1L;
	private final static String BASEURL = "http://localhost:8080/taustapi/v2"; 
	private final static IXLIFFFactory xf = Factory.XOM;

	private final TransAPIClient ttapi;
	
	private JList<String> lbMethods;
	private JTextField edBaseURL;
	private JTextField edId;
	private JTextField edCallbackUrl;
	private JTextField edSrcLang;
	private JTextField edTrgLang;
	private JTextField edSource;
	private JTextField edTarget;
	private JTextArea edResult;
	
	public TausAPIPanel (TransAPIClient ttapi) {
		GridBagLayout layout =  new GridBagLayout();
		setLayout(layout);
		setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		
		this.ttapi = ttapi;
		
		String[] methods = {
			/* 0 */ "GET translation - get a list of all translation requests",
			/* 1 */ "POST translation - create a new translation request",
			/* 2 */ "GET translation/{id} - get an existing translation request",
			/* 3 */ "PUT translation/{id} - modify an existing translation request",
			/* 4 */ "DELETE translation/{id} - delete an existing translation request",
			/* 5 */ "GET status/{id} - get the status of a translation request",
			/* 6 */ "PUT accept/{id} - change the status to 'accepted'",
			/* 7 */ "PUT reject/{id} - change the status to 'rejected'",
			/* 8 */ "PUT confirm/{id} - change the status to 'confirmed'",
			/* 9 */ "PUT cancel/{id} - change the status to 'cancelled'"};
		lbMethods = new JList<>(methods);
		lbMethods.setSelectedIndex(0);
		lbMethods.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged (ListSelectionEvent event) {
				updateCommand();
			}
		});
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0; c.gridy = 0; c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.weightx = 0.10;
		add(new JLabel("Command:"), c);
		c = new GridBagConstraints();
		c.gridx = 1; c.gridy = 0; c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.90;
		add(lbMethods, c);
		JButton btExecute = new JButton("Execute");
		btExecute.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent event) {
				execute();
			}
		});
		c = new GridBagConstraints();
		c.gridx = 3; c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(btExecute, c);
		
		c = new GridBagConstraints();
		c.gridx = 0; c.gridy = 1; c.anchor = GridBagConstraints.LINE_START;
		c.weightx = 0.10;
		c.insets = new Insets(10, 0, 0, 0);
		add(new JLabel("Service URL:"), c);
		c = new GridBagConstraints();
		c.gridx = 1; c.gridy = 1; c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.90; c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(10, -4, 0, 0);
		add((edBaseURL = new JTextField()), c);
		
		edBaseURL.setText(BASEURL);
		ttapi.setBaseURL(edBaseURL.getText());
		
		c = new GridBagConstraints();
		c.gridx = 0; c.gridy = 2; c.anchor = GridBagConstraints.LINE_START;
		c.weightx = 0.10;
		add(new JLabel("Request ID:"), c);
		c = new GridBagConstraints();
		c.insets = new Insets(0, -4, 0, 0);
		c.gridx = 1; c.gridy = 2; c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.90; c.gridwidth = GridBagConstraints.REMAINDER;
		add((edId = new JTextField()), c);
		edId.setText("381c165c-bbbb-eeee-9b53-9fe0decc8a11");
		
		c = new GridBagConstraints();
		c.gridx = 0; c.gridy = 3; c.anchor = GridBagConstraints.LINE_START;
		add(new JLabel("Callback URL:"), c);
		c = new GridBagConstraints();
		c.insets = new Insets(0, -4, 0, 0);
		c.gridx = 1; c.gridy = 3; c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = GridBagConstraints.REMAINDER;
		add((edCallbackUrl = new JTextField()), c);
		
		c = new GridBagConstraints();
		c.gridx = 0; c.gridy = 4; c.anchor = GridBagConstraints.LINE_START;
		add(new JLabel("Source language:"), c);
		c = new GridBagConstraints();
		c.insets = new Insets(0, -4, 0, 0);
		c.gridx = 1; c.gridy = 4; c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = GridBagConstraints.REMAINDER;
		add((edSrcLang = new JTextField()), c);
		edSrcLang.setText("en");
		
		c = new GridBagConstraints();
		c.gridx = 0; c.gridy = 5; c.anchor = GridBagConstraints.LINE_START;
		add(new JLabel("Target language:"), c);
		c = new GridBagConstraints();
		c.gridx = 1; c.gridy = 5; c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(0, -4, 0, 0);
		add((edTrgLang = new JTextField()), c);
		edTrgLang.setText("iu");

		c = new GridBagConstraints();
		c.gridx = 0; c.gridy = 6; c.anchor = GridBagConstraints.LINE_START;
		add(new JLabel("Source text:"), c);
		c = new GridBagConstraints();
		c.insets = new Insets(0, -4, 0, 0);
		c.gridx = 1; c.gridy = 6; c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = GridBagConstraints.REMAINDER;
		add((edSource = new JTextField()), c);

		c = new GridBagConstraints();
		c.gridx = 0; c.gridy = 7; c.anchor = GridBagConstraints.LINE_START;
		add(new JLabel("Target text:"), c);
		c = new GridBagConstraints();
		c.insets = new Insets(0, -4, 10, 0);
		c.gridx = 1; c.gridy = 7; c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = GridBagConstraints.REMAINDER;
		add((edTarget = new JTextField()), c);
		
		edResult = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(edResult);
		edResult.setLineWrap(true);
		edResult.setWrapStyleWord(true);
		edResult.setFont(new Font("Gadugi", 0, 20));
		c = new GridBagConstraints(); c.anchor = GridBagConstraints.PAGE_END;
		c.gridx = 0; c.gridy = 8; c.fill = GridBagConstraints.BOTH;
		c.weighty = 1.0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		add(scrollPane, c);
		
		updateCommand();
	}

	public String getBaseURL () {
		return edBaseURL.getText();
	}

	private void updateCommand () {
		switch ( lbMethods.getSelectedIndex() ) {
		case 0: // GET translation
			enableFields(false, false, false);
			break;
		case 1: // POST translation
			enableFields(true, false, true);
			break;
		case 3: // PUT translation/{id}
			enableFields(true, true, true);
			break;
		default:
			enableFields(true, false, false);
		}
	}
	
	private void enableFields (boolean id,
		boolean target,
		boolean others)
	{
		edId.setEnabled(id);
		edCallbackUrl.setEnabled(others);
		edSrcLang.setEnabled(others);
		edTrgLang.setEnabled(others);
		edSource.setEnabled(others);
		edTarget.setEnabled(target);
	}

	private void execute () {
		ttapi.setBaseURL(edBaseURL.getText());
		ISegment seg;
		try {
			switch ( lbMethods.getSelectedIndex() ) {
			case 0: // GET translation
				ttapi.translation_get();
				break;
			case 1: // POST translation
				seg = xf.createLoneSegment();
				seg.setSource(edSource.getText());
				ttapi.translation_post(edId.getText(), edSrcLang.getText(), edTrgLang.getText(), seg.getSource());
				break;
			case 2: // GET translation/{id}
				ttapi.translation_id_get(edId.getText());
				break;
			case 3: // PUT translation/{id}
				seg = xf.createLoneSegment();
				seg.setSource(edSource.getText());
				seg.setTarget(edTarget.getText());
				ttapi.translation_id_put(edId.getText(), edSrcLang.getText(), edTrgLang.getText(),
					seg.getSource(), seg.getTarget());
				break;
			case 4: // DELETE translation/{id}
				ttapi.translation_id_delete(edId.getText());
				break;
			case 5: // GET status/{id}
				ttapi.status_id_get(edId.getText());
				break;
			case 6: // PUT accept/{id}
				ttapi.accept_id_put(edId.getText());
				break;
			case 7: // PUT reject/{id}
				ttapi.reject_id_put(edId.getText());
				break;
			case 8: // PUT confirm/{id}
				ttapi.confirm_id_put(edId.getText());
				break;
			case 9: // PUT cancel/{id}
				ttapi.cancel_id_put(edId.getText());
				break;
			}

			Response resp = ttapi.getResponse();
			edResult.setText("Result: "
				+ resp.getStatus()
				+ "\n"
				+ resp.readEntity(String.class));
		}
		catch ( Throwable e ) {
			edResult.setText("Exception:\n"+e.getMessage());
		}
		
	}
	
}
