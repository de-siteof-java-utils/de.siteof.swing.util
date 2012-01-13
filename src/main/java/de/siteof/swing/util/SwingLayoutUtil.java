package de.siteof.swing.util;

import java.awt.Component;

import javax.swing.GroupLayout;
import javax.swing.JPanel;
import javax.swing.GroupLayout.Alignment;

public class SwingLayoutUtil {

	public static Component getVerticalPanel(Component ...components) {
		JPanel groupPanel = new JPanel();

		GroupLayout layout = new GroupLayout(groupPanel);
		groupPanel.setLayout(layout);

		GroupLayout.SequentialGroup hGroup =
			layout.createSequentialGroup();
		GroupLayout.SequentialGroup vGroup =
			layout.createSequentialGroup();

		GroupLayout.ParallelGroup parallelGroup = layout.createParallelGroup();
		for (Component component: components) {
			parallelGroup.addComponent(component);
		}
		hGroup.addGroup(parallelGroup);

		for (Component component: components) {
			vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).
		            addComponent(component));
		}

		layout.setHorizontalGroup(hGroup);
		layout.setVerticalGroup(vGroup);

		return groupPanel;
	}

}
