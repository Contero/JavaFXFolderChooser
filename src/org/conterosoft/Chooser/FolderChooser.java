package org.conterosoft.Chooser;

import java.io.File;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;

public class FolderChooser extends Stage 
{
	private File choosen;
	private File currentFolder = null;
	private File initialFolder = null;

	public FolderChooser()
	{
		Button up,
		select,
		open,
		cancel;
		HBox buttons;
		VBox mainVbox;
		ListView<File> filelist;
		Scene scene;

		if (System.getProperty("os.name").equals("Linux"))
		{
			initialFolder = new File("/");
		}
		else
		{
			initialFolder = new File("C:\\");
		}

		currentFolder = initialFolder;

		up = new Button ("Up");
		select = new Button("Select");
		open = new Button("Open");
		cancel = new Button("Cancel");

		filelist = new ListView<File>(getfiles(initialFolder));

		filelist.setOnMouseClicked((event) ->
		{
			if (event.getClickCount() == 2)
			{
				setCurrentFolder(filelist.getSelectionModel().getSelectedItem());
				filelist.getItems().clear();
				filelist.getItems().addAll(getfiles(getCurrentFolder()));
				if(up.isDisabled())
				{
					up.setDisable(false);
				}
			}
		});

		select.setOnAction((event) -> {
			choosen = filelist.getSelectionModel().getSelectedItem();
			this.hide();
		});

		open.setOnAction(event -> {
			setCurrentFolder(filelist.getSelectionModel().getSelectedItem());
			filelist.getItems().clear();
			filelist.getItems().addAll(getfiles(getCurrentFolder()));
			if(up.isDisabled())
			{
				up.setDisable(false);
			}

		});

		up.setOnAction(event -> {
			File current = getCurrentFolder();
			current = current.getParentFile();
			if (current.equals(initialFolder)) 
			{
				up.setDisable(true);
			}
			setCurrentFolder(current);
			filelist.getItems().clear();
			filelist.getItems().addAll(getfiles(getCurrentFolder()));
		});
		up.setDisable(true);

		cancel.setOnAction(event -> {
			choosen = null;
			this.hide();
		});

		buttons = new HBox(5, select, open, cancel);
		mainVbox = new VBox(5, up, filelist, buttons);

		scene = new Scene(mainVbox);
		this.setScene(scene);
	}

	/*
	 * shows window
	 */
	public File showDialog(Window owner)
	{
		if (owner != null)
		{
			this.initOwner(owner);
			this.showAndWait();
		} 
		else
		{
			this.show();
		}
		
		return choosen;
	}

	/*
	 * accepts a folder
	 * returns an observable list of subfolders in that folder
	 */
	private ObservableList<File> getfiles(File folder)
	{

		File[] files = folder.listFiles();

		ObservableList<File> folders = FXCollections.observableArrayList();
		try
		{


			for (File file : files )
			{
				if (file.isDirectory() && !file.isHidden() && file.canRead())
				{
					folders.add(file);
				}
			}
		}
		catch (Exception E)
		{
			//do nothing, leave folders empty
		}
		
		return folders;
	}

	/*
	 * sets value of current folder
	 */
	private void setCurrentFolder(File file)
	{
		currentFolder = file;
	}

	/*
	 * gets current folder
	 */
	private File getCurrentFolder()
	{
		return currentFolder;
	}
	
	/*
	 * returns initial folder
	 */
	public File getInitialDirectory() 
	{
		return initialFolder;
	}
	
	public void setInitialDirectory(File value) 
	{
		initialFolder = value;
		currentFolder = value;
	}

}
