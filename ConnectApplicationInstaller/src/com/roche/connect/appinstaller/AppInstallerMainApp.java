package com.roche.connect.appinstaller;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.validator.routines.DomainValidator;
import org.apache.commons.validator.routines.InetAddressValidator;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

public class AppInstallerMainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
	private boolean isServerAddressAvalible;
	private boolean validPort;
	private boolean validUserName;
	private boolean validPassword;
	private String serverAddress;
	
	private boolean isFQDNavailabe;
	private boolean isDomainAvailable;
	
	private boolean isPSQLAdress;
	private boolean isPSQLPort;
	private boolean isPSQLUser;
	private boolean isPSQLPassword;
	
	private boolean isFirstName;
	private boolean isLastName;
	private boolean isAdminEmail;
	private boolean isUserName;
	private boolean isPassword;
	private boolean isConformPassword;
	private Properties configPropMap = ConfigurationProperties.propertiesMap;
	
	SaveYamlProperties saveYamlProperties = new SaveYamlProperties(); 

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Connect System Installer");
		this.primaryStage.getIcons().add(new Image("resources/Packager_Install_Icon.png"));
		ConfigurationProperties.loadProperties();
		initRootLayout();
		showWelcomeScreen();
	}

	/**
	 * Initializes the root layout.
	 */
	public void initRootLayout() {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(AppInstallerMainApp.class.getResource("view/AppInstallerRootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showWelcomeScreen() {
		try {
			// Load person overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(AppInstallerMainApp.class.getResource("view/WelcomeView.fxml"));
			Pane personOverview = (Pane) loader.load();
			getPrimaryStage().setOnCloseRequest(confirmCloseEventHandler);

			// Set person overview into the center of root layout.
			rootLayout.setCenter(personOverview);
			((Button) ((HBox) ((Pane) personOverview.getChildren().get(0)).getChildren().get(0)).getChildren().get(1))
					.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent arg0) {
							getPrimaryStage().close();
//							openNIPTSettings();
							openConfigSettings();

						}
					});

			((Button) ((HBox) ((Pane) personOverview.getChildren().get(0)).getChildren().get(0)).getChildren().get(2))
					.setOnAction(event -> getPrimaryStage()
							.fireEvent(new WindowEvent(getPrimaryStage(), WindowEvent.WINDOW_CLOSE_REQUEST)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showNIPTSettingsScreen() {
		try {
			// Load person overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(AppInstallerMainApp.class.getResource("view/NIPTSettingsView.fxml"));
			Pane personOverview = (Pane) loader.load();
			getPrimaryStage().setOnCloseRequest(confirmCloseEventHandler);

			// Set person overview into the center of root layout.
			rootLayout.setCenter(personOverview);
			((Button) ((HBox) ((Pane) personOverview.getChildren().get(0)).getChildren().get(0)).getChildren().get(1))
					.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent arg0) {
							getPrimaryStage().close();
							openConfigSettings();

						}
					});
			((Button) ((HBox) ((Pane) personOverview.getChildren().get(0)).getChildren().get(0)).getChildren().get(0))
					.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent arg0) {
							configPropMap.setProperty("fetalSexAvailability", (((RadioButton) ((Pane) personOverview.getChildren().get(0)).getChildren().get(4)).isSelected() ? "Available" : "Not Available"));
							getPrimaryStage().close();
							openWelcomeScreen();

						}
					});

			((Button) ((HBox) ((Pane) personOverview.getChildren().get(0)).getChildren().get(0)).getChildren().get(2))
					.setOnAction(event -> getPrimaryStage()
							.fireEvent(new WindowEvent(getPrimaryStage(), WindowEvent.WINDOW_CLOSE_REQUEST)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showConfigSettingsScreen() {
		try {
			// Load person overview.
			FXMLLoader loader1 = new FXMLLoader();
			loader1.setLocation(AppInstallerMainApp.class.getResource("view/ConfigurationSettingsView.fxml"));
			Pane personOverview = (Pane) loader1.load();
			getPrimaryStage().setOnCloseRequest(confirmCloseEventHandler);

			// Set person overview into the center of root layout.
			rootLayout.setCenter(personOverview);
			TextField problemReport = ((TextField) ((Pane) personOverview.getChildren().get(0)).getChildren().get(5));
			TextField backupPath = ((TextField) ((Pane) personOverview.getChildren().get(0)).getChildren().get(9));
			problemReport.setText(configPropMap.getProperty("problemReportPath",""));
			backupPath.setText(configPropMap.getProperty("backupPath",""));
			((Button) ((HBox) ((Pane) personOverview.getChildren().get(0)).getChildren().get(0)).getChildren().get(0))
					.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent arg0) {
							getPrimaryStage().close();
//							openNIPTSettings();
							openWelcomeScreen();

						}
					});
			if(problemReport.getText().length()==0 || backupPath.getText().length()==0)
				((Button) ((HBox) ((Pane) personOverview.getChildren().get(0)).getChildren().get(0)).getChildren().get(1)).setDisable(true);

			((Button) ((HBox) ((Pane) personOverview.getChildren().get(0)).getChildren().get(0)).getChildren().get(1))
					.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent arg0) {
							configPropMap.setProperty("problemReportPath", problemReport.getText());
							configPropMap.setProperty("backupPath", backupPath.getText());
							getPrimaryStage().close();
							openLanguageSettings();
						}
					});

			((Button) ((HBox) ((Pane) personOverview.getChildren().get(0)).getChildren().get(0)).getChildren().get(2))
					.setOnAction(event -> getPrimaryStage()
							.fireEvent(new WindowEvent(getPrimaryStage(), WindowEvent.WINDOW_CLOSE_REQUEST)));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showLanguageSettingsScreen() {
		try {
			// Load person overview.
			FXMLLoader loader1 = new FXMLLoader();
			loader1.setLocation(AppInstallerMainApp.class.getResource("view/LanguageLocalizationView.fxml"));
			Pane personOverview = (Pane) loader1.load();
			getPrimaryStage().setOnCloseRequest(confirmCloseEventHandler);
			ComboBox<String> languageCombo = ((ComboBox<String>) ((Pane) personOverview.getChildren().get(0)).getChildren().get(5));
			ComboBox<String> dateCombo = ((ComboBox<String>) ((Pane) personOverview.getChildren().get(0)).getChildren().get(6));
			ObservableList<String> languageOptions = FXCollections.observableArrayList("English-US");
			languageCombo.getItems().addAll(languageOptions);
			languageCombo.getSelectionModel().select(configPropMap.getProperty("systemLanguage",""));
			if(languageCombo.getSelectionModel().getSelectedItem().length()==0)
				languageCombo.getSelectionModel().select(0);
			
			ObservableList<String> dateOptions = FXCollections.observableArrayList("DD/MM/YYYY", "MM/DD/YYYY");
			dateCombo.getItems().addAll(dateOptions);
			dateCombo.getSelectionModel().select(configPropMap.getProperty("dateFormat",""));
			if(dateCombo.getSelectionModel().getSelectedItem().length()==0)
				dateCombo.getSelectionModel().select(0);
			// Set person overview into the center of root layout.
			rootLayout.setCenter(personOverview);
			
			((Button) ((HBox) ((Pane) personOverview.getChildren().get(0)).getChildren().get(0)).getChildren().get(0))
					.setOnAction(new EventHandler<ActionEvent>() {
						
						@Override
						public void handle(ActionEvent arg0) {
							getPrimaryStage().close();
							openConfigSettings();
						}
					});

			((Button) ((HBox) ((Pane) personOverview.getChildren().get(0)).getChildren().get(0)).getChildren().get(1))
					.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent arg0) {
								configPropMap.setProperty("systemLanguage", languageCombo.getSelectionModel().getSelectedItem());
								configPropMap.setProperty("dateFormat", dateCombo.getSelectionModel().getSelectedItem());
								getPrimaryStage().close();
								openAdminUserScreen();

						}
					});

			((Button) ((HBox) ((Pane) personOverview.getChildren().get(0)).getChildren().get(0)).getChildren().get(2))
					.setOnAction(event -> getPrimaryStage()
							.fireEvent(new WindowEvent(getPrimaryStage(), WindowEvent.WINDOW_CLOSE_REQUEST)));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showSMTPSettingsScreen() {
		try {
			// Load person overview.
			FXMLLoader loader1 = new FXMLLoader();
			loader1.setLocation(AppInstallerMainApp.class.getResource("view/SMTPMailTransferViewer.fxml"));
			Pane personOverview = (Pane) loader1.load();
			getPrimaryStage().setOnCloseRequest(confirmCloseEventHandler);
			// Set person overview into the center of root layout.
			rootLayout.setCenter(personOverview);
			TextField mailServer = ((TextField) ((Pane) personOverview.getChildren().get(0)).getChildren().get(8));
			TextField mailPort = ((TextField) ((Pane) personOverview.getChildren().get(0)).getChildren().get(9));
			TextField mailUserName = ((TextField) ((Pane) personOverview.getChildren().get(0)).getChildren().get(12));
			TextField mailPassword = ((TextField) ((Pane) personOverview.getChildren().get(0)).getChildren().get(13));
			TextField mailSenderMail = ((TextField) ((Pane) personOverview.getChildren().get(0)).getChildren().get(7));
			RadioButton passwordAuthEnabled = ((RadioButton) ((Pane) personOverview.getChildren().get(0)).getChildren().get(10));
			RadioButton passwordAuthDisabled = ((RadioButton) ((Pane) personOverview.getChildren().get(0)).getChildren().get(11));
			mailServer.setText(configPropMap.getProperty("mailServer",""));
			if(mailServer.getText().length() > 0) {
				isServerAddressAvalible = true;
			}else {
				isServerAddressAvalible = false;
			}
			mailPort.setText(configPropMap.getProperty("mailPort",""));
			if(mailPort.getText().length() > 0) {
				validPort = true;
			}else {
				validPort = false;
			}
			mailUserName.setText(configPropMap.getProperty("mailUserName",""));
			if(mailUserName.getText().length() > 0) {
				validUserName = true;
			}else {
				validUserName = false;
			}
			mailSenderMail.setText(configPropMap.getProperty("mailSenderMail",""));
			validPassword = false;
			
			ToggleGroup toggleGroup = new ToggleGroup(); 
			passwordAuthEnabled.setToggleGroup(toggleGroup);
			passwordAuthDisabled.setToggleGroup(toggleGroup);
			
			if(mailServer.getText().length()==0 || mailPort.getText().length()==0 || mailUserName.getText().length()==0 ||mailPassword.getText().length()==0)
				((Button) ((HBox) ((Pane) personOverview.getChildren().get(0)).getChildren().get(0)).getChildren().get(1)).setDisable(true);
			
			((Button) ((HBox) ((Pane) personOverview.getChildren().get(0)).getChildren().get(0)).getChildren().get(0))
					.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent arg0) {
							getPrimaryStage().close();
							openAdminUserScreen();
						}
					});

			((Button) ((HBox) ((Pane) personOverview.getChildren().get(0)).getChildren().get(0)).getChildren().get(2))
					.setOnAction(event -> getPrimaryStage()
							.fireEvent(new WindowEvent(getPrimaryStage(), WindowEvent.WINDOW_CLOSE_REQUEST)));


			mailServer.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {

						@Override
						public void handle(KeyEvent keyEvent) {
							if (mailServer.getText().length() != 0 || (!keyEvent.getCharacter().isEmpty() && !keyEvent.getCharacter().equals("\b"))) {
								isServerAddressAvalible = true;
							} else {
								isServerAddressAvalible = false;
							}
							enableNext(personOverview);

						}
					});

			mailPort.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
						@Override
						public void handle(KeyEvent keyEvent) {
							if (mailPort.getText().length() == 0) {
								validPort = false;
							}
							if (!"0123456789".contains(keyEvent.getCharacter())) {
								keyEvent.consume();
							} else {
								validPort = true;
							}
							enableNext(personOverview);
						}
					});

			mailUserName.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {

						@Override
						public void handle(KeyEvent keyEvent) {
							if (mailUserName.getText().length() != 0 || (!keyEvent.getCharacter().isEmpty() && !keyEvent.getCharacter().equals("\b"))) {
								validUserName = true;
							} else {
								validUserName = false;
							}

							enableNext(personOverview);
						}
					});

			mailPort.textProperty().addListener(new ChangeListener<String>() {

						@Override
						public void changed(ObservableValue<? extends String> observable, String oldValue,
								String newValue) {
							try {
								final int intValue = Integer.parseInt(newValue);

								if (intValue < 0 || intValue > 65535) {
									mailPort.textProperty().setValue(oldValue);
								}
							} catch (Exception e) {

							}
						}

					});

			((Button) ((HBox) ((Pane) personOverview.getChildren().get(0)).getChildren().get(0)).getChildren().get(1))
					.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent arg0) {
							try {
								String email = mailUserName.getText();
								int port = Integer.valueOf(mailPort.getText());
								int pos = email.indexOf('@');

								if (pos == -1)
									throw new Exception("Check your email credentials and try again.");
								serverAddress = mailServer.getText();
								if(!AppInstallerController.isAddressValid(email, serverAddress, port)) {
									throw new Exception();
								}
								((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(19)).setVisible(false);
								configPropMap.setProperty("mailServer", serverAddress);
								configPropMap.setProperty("mailPort", mailPort.getText());
								configPropMap.setProperty("mailUserName", email);
								configPropMap.setProperty("mailPassword", mailPassword.getText());
								configPropMap.setProperty("mailSenderMail", mailSenderMail.getText());
								getPrimaryStage().close();
								openDomainScreen();
							}catch(Exception e) {
								((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(19)).setText(e.getMessage());
								((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(19)).setVisible(true);
							}
							
						}
					});
			
			mailPassword.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {

				@Override
				public void handle(KeyEvent keyEvent) {
					if (mailPassword.getText().length() != 0 || (!keyEvent.getCharacter().isEmpty() && !keyEvent.getCharacter().equals("\b"))) {
						validPassword = true;
					} else {
						validPassword = false;
					}

					enableNext(personOverview);
				}
			});
			
			toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
				public void changed(ObservableValue<? extends Toggle> ob, Toggle o, Toggle n) {

					RadioButton radioButton = (RadioButton) toggleGroup.getSelectedToggle();

					if (radioButton != null) {
						if(radioButton.getText().equals("No")) {
							mailUserName.setText("");
							mailUserName.setDisable(true);
							mailPassword.setText("");
							mailPassword.setDisable(true);
						}else if(radioButton.getText().equals("Yes")) {
							mailUserName.setDisable(false);
							mailPassword.setDisable(false);
						}

					}
				}
			});

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showDomainNameScreen() {
		try {
			// Load person overview.
			FXMLLoader loader1 = new FXMLLoader();
			loader1.setLocation(AppInstallerMainApp.class.getResource("view/DominNameView.fxml"));
			Pane personOverview = (Pane) loader1.load();
			getPrimaryStage().setOnCloseRequest(confirmCloseEventHandler);

			// Set person overview into the center of root layout.
			rootLayout.setCenter(personOverview);
			TextField fqdn_txt = ((TextField) ((Pane) personOverview.getChildren().get(0)).getChildren().get(5));
			TextField labDomain_txt = ((TextField) ((Pane) personOverview.getChildren().get(0)).getChildren().get(6));
			fqdn_txt.setText(configPropMap.getProperty("fqdnserver",""));
			if(fqdn_txt.getText().length() > 0) {
				isFQDNavailabe = true;
			}
			labDomain_txt.setText(configPropMap.getProperty("labDomain",""));
			if(labDomain_txt.getText().length() > 0) {
				isDomainAvailable = true;
			}
			((Button) ((HBox) ((Pane) personOverview.getChildren().get(0)).getChildren().get(0)).getChildren().get(0))
					.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent arg0) {
							getPrimaryStage().close();
							openSMTPSettings();

						}
					});
			
			fqdn_txt.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {

				@Override
				public void handle(KeyEvent keyEvent) {
					if (fqdn_txt.getText().length() != 0 || (!keyEvent.getCharacter().isEmpty() && !keyEvent.getCharacter().equals("\b"))) {
						isFQDNavailabe = true;
						enableNextForFQDN(personOverview);
						
					}else {
						isFQDNavailabe = false;
						enableNextForFQDN(personOverview);
					}
				}
			});
			
			labDomain_txt.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {

				@Override
				public void handle(KeyEvent keyEvent) {
					if (labDomain_txt.getText().length() != 0 || (!keyEvent.getCharacter().isEmpty() && !keyEvent.getCharacter().equals("\b"))) {
						isDomainAvailable = true;
						enableNextForFQDN(personOverview);
						
					}else {
						isDomainAvailable = false;
						enableNextForFQDN(personOverview);
					}
				}
			});
			
			fqdn_txt.focusedProperty().addListener(new ChangeListener<Boolean>() {

				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					try {
						if((!newValue) && AppInstallerController.validateFQDN(fqdn_txt.getText())) {
							((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(7)).setVisible(false);
							isFQDNavailabe = true;
						}
					} catch (Exception e) {
						((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(7)).setText(e.getMessage());
						((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(7)).setVisible(true);
						isDomainAvailable = false;
					}
					enableNextForFQDN(personOverview);
				}
			});			
			
			labDomain_txt.focusedProperty().addListener(new ChangeListener<Boolean>() {

				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					try {
						if((!newValue) && AppInstallerController.validateLabDomain(labDomain_txt.getText())) {
							((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(8)).setVisible(false);
							isDomainAvailable = true;
						}
					} catch (Exception e) {
						((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(8)).setText(e.getMessage());
						((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(8)).setVisible(true);
						isDomainAvailable = false;
					}
					enableNextForFQDN(personOverview);
				}
			});			

			((Button) ((HBox) ((Pane) personOverview.getChildren().get(0)).getChildren().get(0)).getChildren().get(1))
					.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent arg0) {
										configPropMap.setProperty("fqdnserver", fqdn_txt.getText());
										configPropMap.setProperty("labDomain", labDomain_txt.getText());
										getPrimaryStage().close();
										openCertificateScreen();
									}
					});

			((Button) ((HBox) ((Pane) personOverview.getChildren().get(0)).getChildren().get(0)).getChildren().get(2))
					.setOnAction(event -> getPrimaryStage()
							.fireEvent(new WindowEvent(getPrimaryStage(), WindowEvent.WINDOW_CLOSE_REQUEST)));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showCertificateScreen() {
		boolean ls;
		try {
			// Load person overview.
			FXMLLoader loader1 = new FXMLLoader();
			loader1.setLocation(AppInstallerMainApp.class.getResource("view/SSLCertificateView.fxml"));
			Pane personOverview = (Pane) loader1.load();
			getPrimaryStage().setOnCloseRequest(confirmCloseEventHandler);

			// Set person overview into the center of root layout.
			rootLayout.setCenter(personOverview);
			TextField certificatePath = ((TextField) ((Pane) personOverview.getChildren().get(0)).getChildren().get(4));
			
			certificatePath.setText(configPropMap.getProperty("sslKeyStore",""));
			RadioButton passwordAuthEnabled = ((RadioButton) ((Pane) personOverview.getChildren().get(0)).getChildren().get(8));
			RadioButton passwordAuthDisabled = ((RadioButton) ((Pane) personOverview.getChildren().get(0)).getChildren().get(9));
			System.out.println(passwordAuthDisabled.getText());
			System.out.println(passwordAuthEnabled.getText());
			PasswordField sslpassword = ((PasswordField) ((Pane) personOverview.getChildren().get(0)).getChildren().get(11));
			ToggleGroup toggleGroup = new ToggleGroup(); 
			passwordAuthEnabled.setToggleGroup(toggleGroup);
			passwordAuthDisabled.setToggleGroup(toggleGroup);
				toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
				public void changed(ObservableValue<? extends Toggle> ob, Toggle o, Toggle n) {

					 RadioButton 	radioButton = (RadioButton) toggleGroup.getSelectedToggle();

					if (radioButton != null) {
						if(radioButton.getText().equals("No")) {
							sslpassword.setText("");
							sslpassword.setDisable(true);
							
						}else if(radioButton.getText().equals("Yes")) {
							sslpassword.setDisable(false);
						
						}

					}
				}
			});
			((Button) ((HBox) ((Pane) personOverview.getChildren().get(0)).getChildren().get(0)).getChildren().get(0))
					.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent arg0) {
							getPrimaryStage().close();
							openDomainScreen();
						}
					});
			
			if(certificatePath.getText().length() == 0 && sslpassword.getText()!=null)
				((Button) ((HBox) ((Pane) personOverview.getChildren().get(0)).getChildren().get(0)).getChildren().get(1)).setDisable(true);

			((Button) ((HBox) ((Pane) personOverview.getChildren().get(0)).getChildren().get(0)).getChildren().get(1))
					.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent arg0) {
							try {
								if (certificatePath.getText() != null && !certificatePath.getText().isEmpty() && AppInstallerController.impCert(certificatePath.getText(),"",sslpassword.getText())) {
									((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(6)).setVisible(false);
									configPropMap.setProperty("sslKeyStore", certificatePath.getText());
									
									getPrimaryStage().close();
									openDatabaseScreen();
								}
							} catch (Exception e) {
								if(e.getMessage()!=null) {
									if(Arrays.asList("Access denied while importing  the certificate.","FQDN is not matching with Common Name of the key.","Certificate is corrupted or empty, please upload valid certificate.").contains(e.getMessage())) {
										((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(6)).setText(e.getMessage());
										((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(6)).setVisible(true);
									}else {
										((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(6)).setText(e.getMessage());
										((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(6)).setVisible(true);
									}
								}else {
									((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(6)).setText("Certificate is corrupted or empty, please upload valid certificate.");
									((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(6)).setVisible(true);
								}
								
							}
						}
					});
			
			((Button) ((HBox) ((Pane) personOverview.getChildren().get(0)).getChildren().get(0)).getChildren().get(2))
					.setOnAction(event -> getPrimaryStage()
							.fireEvent(new WindowEvent(getPrimaryStage(), WindowEvent.WINDOW_CLOSE_REQUEST)));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void showDatabaseScreen() {
		try {
			// Load person overview.
			FXMLLoader loader1 = new FXMLLoader();
			loader1.setLocation(AppInstallerMainApp.class.getResource("view/DatabaseConfigView.fxml"));
			Pane personOverview = (Pane) loader1.load();
			getPrimaryStage().setOnCloseRequest(confirmCloseEventHandler);

			// Set person overview into the center of root layout.
			rootLayout.setCenter(personOverview);
			TextField psqlAddress =((TextField) ((Pane) personOverview.getChildren().get(0)).getChildren().get(4));
			TextField psqlPort =((TextField) ((Pane) personOverview.getChildren().get(0)).getChildren().get(8));
			TextField psqlUserName =((TextField) ((Pane) personOverview.getChildren().get(0)).getChildren().get(9));
			TextField psqlPassword =((TextField) ((Pane) personOverview.getChildren().get(0)).getChildren().get(11));
			psqlAddress.setText(configPropMap.getProperty("dbAddress",""));
			if(psqlAddress.getText().length()<=0) 
				psqlAddress.setText(InetAddress.getLocalHost().getHostAddress());
			isPSQLAdress = true;
						
			psqlPort.setText(configPropMap.getProperty("dbPort","5432"));
			if(psqlPort.getText().length()>0) 
				isPSQLPort = true;
			else 
				isPSQLPort = false;
			
			psqlUserName.setText(configPropMap.getProperty("dbUsername",""));
			if(psqlUserName.getText().length()>0) 
				isPSQLUser = true;
			else 
				isPSQLUser = false;
			isPSQLPassword = false;
			psqlPort.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent keyEvent) {
					if (psqlPort.getText().length() == 0) {
						isPSQLPort=false;
					}
					if (!"0123456789".contains(keyEvent.getCharacter())) {
						keyEvent.consume();
					}else {
						isPSQLPort=true;
					}
				}
			});
			
			psqlPort.textProperty().addListener(new ChangeListener<String>() {

				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue,
						String newValue) {
					try {
						final int intValue = Integer.parseInt(newValue);

						if (intValue < 0 || intValue > 65535) {
							psqlPort.textProperty().setValue(oldValue);
						}
					} catch (Exception e) {

					}
				}

			});
			((Button) ((HBox) ((Pane) personOverview.getChildren().get(0)).getChildren().get(0)).getChildren().get(0))
					.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent arg0) {
							getPrimaryStage().close();
							openCertificateScreen();

						}
					});
			if(psqlAddress.getText().length() == 0 || psqlPort.getText().length() == 0 || psqlUserName.getText().length() == 0 || psqlPassword.getText().length() == 0)
			((Button) ((HBox) ((Pane) personOverview.getChildren().get(0)).getChildren().get(0)).getChildren().get(1)).setDisable(true);
			
			
			psqlAddress.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {

				@Override
				public void handle(KeyEvent keyEvent) {
					if (psqlAddress.getText().length() != 0 || (!keyEvent.getCharacter().isEmpty() && !keyEvent.getCharacter().equals("\b"))) {
						isPSQLAdress = true;
						enableNextForDBConfig(personOverview);
						
					}else {
						isPSQLAdress = false;
						enableNextForDBConfig(personOverview);
					}
				}
			});
			
			psqlAddress.focusedProperty().addListener(new ChangeListener<Boolean>() {

				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					if((!newValue) && (!IPValidation(psqlAddress.getText()))) {
						((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(13)).setVisible(true);
					}else {
						((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(13)).setVisible(false);
					}
				}
			});			
			psqlUserName.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {

				@Override
				public void handle(KeyEvent keyEvent) {
					if (psqlUserName.getText().length() != 0 || (!keyEvent.getCharacter().isEmpty() && !keyEvent.getCharacter().equals("\b"))) {
						isPSQLUser = true;
						enableNextForDBConfig(personOverview);
						
					}else {
						isPSQLUser = false;
						enableNextForDBConfig(personOverview);
					}
				}
			});
			
			psqlPassword.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {

				@Override
				public void handle(KeyEvent keyEvent) {
					if (psqlPassword.getText().length() != 0 || (!keyEvent.getCharacter().isEmpty() && !keyEvent.getCharacter().equals("\b"))) {
						isPSQLPassword = true;
						enableNextForDBConfig(personOverview);
						
					}else {
						isPSQLPassword = false;
						enableNextForDBConfig(personOverview);
					}
				}
			});
			

			((Button) ((HBox) ((Pane) personOverview.getChildren().get(0)).getChildren().get(0)).getChildren().get(1))
					.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent arg0) {
							configPropMap.setProperty("dbAddress", psqlAddress.getText());
							configPropMap.setProperty("dbPort", psqlPort.getText());
							configPropMap.setProperty("dbUsername", psqlUserName.getText());
							configPropMap.setProperty("dbPassword", psqlPassword.getText());
							try {
								dbValidation();
								((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(14)).setVisible(false);
								getPrimaryStage().close();
								openCompleteScreen();
							} catch (Exception e) {
								((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(14)).setVisible(true);
							}
						}
					});

			((Button) ((HBox) ((Pane) personOverview.getChildren().get(0)).getChildren().get(0)).getChildren().get(2))
					.setOnAction(event -> getPrimaryStage()
							.fireEvent(new WindowEvent(getPrimaryStage(), WindowEvent.WINDOW_CLOSE_REQUEST)));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void showServerConfigScreen() {
		try {
			// Load person overview.
			FXMLLoader loader1 = new FXMLLoader();
			loader1.setLocation(AppInstallerMainApp.class.getResource("view/ServerConfigurationView.fxml"));
			Pane personOverview = (Pane) loader1.load();
			getPrimaryStage().setOnCloseRequest(confirmCloseEventHandler);

			// Set person overview into the center of root layout.
			rootLayout.setCenter(personOverview);
			((Button) ((HBox) ((Pane) personOverview.getChildren().get(0)).getChildren().get(0)).getChildren().get(0))
					.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent arg0) {
							getPrimaryStage().close();
							openDatabaseScreen();

						}
					});

			((Button) ((HBox) ((Pane) personOverview.getChildren().get(0)).getChildren().get(0)).getChildren().get(1))
					.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent arg0) {
							getPrimaryStage().close();
							showCompleteScreen();
						}
					});

			((Button) ((HBox) ((Pane) personOverview.getChildren().get(0)).getChildren().get(0)).getChildren().get(2))
					.setOnAction(event -> getPrimaryStage()
							.fireEvent(new WindowEvent(getPrimaryStage(), WindowEvent.WINDOW_CLOSE_REQUEST)));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void showCompleteScreen() {
		try {
			// Load person overview.
			FXMLLoader loader1 = new FXMLLoader();
			loader1.setLocation(AppInstallerMainApp.class.getResource("view/CompleteView.fxml"));
			Pane personOverview = (Pane) loader1.load();
			getPrimaryStage().setOnCloseRequest(confirmCloseEventHandler);
			String versionNumber=saveYamlProperties.getFilepath("build_number")+"."+saveYamlProperties.getFilepath("major_version")+"."+saveYamlProperties.getFilepath("minor_verion");
			((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(3)).setText("Avenio connect "+versionNumber+" has been successfully installed.");
			String loginURL="https://"+configPropMap.getProperty("fqdnserver")+":8080/connect_ui/#/login";
			Hyperlink link = ((Hyperlink) ((Pane) personOverview.getChildren().get(0)).getChildren().get(5));
			link.setText(loginURL);
			link.setOnAction(new EventHandler<ActionEvent>() {

	            @Override
	            public void handle(ActionEvent t) {
	                getHostServices().showDocument(link.getText());
	            }

	        });
			String displayMessage="Please run the below script before login and wait till the status of all microservices is having value as “yes.”\n\n" + 
					"/opt/roche/connect/scripts/connect_status.sh\n" ;
			((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(6)).setText(displayMessage);
			// Set person overview into the center of root layout.
			rootLayout.setCenter(personOverview);
			((Button) ((HBox) ((Pane) personOverview.getChildren().get(0)).getChildren().get(0)).getChildren().get(0))
					.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent arg0) {
							getPrimaryStage().close();
							openDatabaseScreen();

						}
					});

			((Button) ((HBox) ((Pane) personOverview.getChildren().get(0)).getChildren().get(0)).getChildren().get(1))
					.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent arg0) {
							try {
								confirmSavingDetails();
							}catch (Exception e) {
								Alert a = new Alert(AlertType.ERROR);
								a.setTitle("Error");
								a.setHeaderText("Error while saving data to yaml");
								a.show();
							}
						}
					});

			((Button) ((HBox) ((Pane) personOverview.getChildren().get(0)).getChildren().get(0)).getChildren().get(2))
					.setOnAction(event -> getPrimaryStage()
							.fireEvent(new WindowEvent(getPrimaryStage(), WindowEvent.WINDOW_CLOSE_REQUEST)));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	

	private void enableNext(Pane personOverview) {
		if (validPort && validUserName && isServerAddressAvalible && validPassword) {
			((Button) ((HBox) ((Pane) personOverview.getChildren().get(0)).getChildren().get(0)).getChildren().get(1))
					.setDisable(false);
		} else {
			((Button) ((HBox) ((Pane) personOverview.getChildren().get(0)).getChildren().get(0)).getChildren().get(1))
					.setDisable(true);
		}
	}
	
	private void enableNextForFQDN(Pane personOverview) {
		if (isFQDNavailabe && isDomainAvailable) {
			((Button) ((HBox) ((Pane) personOverview.getChildren().get(0)).getChildren().get(0)).getChildren().get(1))
					.setDisable(false);
		} else {
			((Button) ((HBox) ((Pane) personOverview.getChildren().get(0)).getChildren().get(0)).getChildren().get(1))
					.setDisable(true);
		}
	}
	
	private void enableNextForDBConfig(Pane personOverview) {
		if (isPSQLAdress && isPSQLPort && isPSQLUser && isPSQLPassword) {
			((Button) ((HBox) ((Pane) personOverview.getChildren().get(0)).getChildren().get(0)).getChildren().get(1))
					.setDisable(false);
		} else {
			((Button) ((HBox) ((Pane) personOverview.getChildren().get(0)).getChildren().get(0)).getChildren().get(1))
					.setDisable(true);
		}
	}
	
	private void enableNextForAdminUsesr(Pane personOverview) {
		if (isFirstName && isLastName && isUserName&&isAdminEmail && isPassword && isConformPassword) {
			((Button) ((HBox) ((Pane) personOverview.getChildren().get(0)).getChildren().get(0)).getChildren().get(1))
					.setDisable(false);
		} else {
			((Button) ((HBox) ((Pane) personOverview.getChildren().get(0)).getChildren().get(0)).getChildren().get(1))
					.setDisable(true);
		}
	}


	private void openWelcomeScreen() {
		getPrimaryStage().close();
		createStage();
		showWelcomeScreen();
	}

	private void openNIPTSettings() {
		getPrimaryStage().close();
		createStage();
		showNIPTSettingsScreen();
	}

	private void openConfigSettings() {
		getPrimaryStage().close();
		createStage();
		showConfigSettingsScreen();
	}

	private void openLanguageSettings() {
		getPrimaryStage().close();
		createStage();
		showLanguageSettingsScreen();
	}

	private void openSMTPSettings() {
		getPrimaryStage().close();
		createStage();
		showSMTPSettingsScreen();
	}
	
	private void openDomainScreen() {
		getPrimaryStage().close();
		createStage();
		showDomainNameScreen();
	}
	
	private void openCertificateScreen() {
		getPrimaryStage().close();
		createStage();
		showCertificateScreen();
	}
	private void openDatabaseScreen() {
		getPrimaryStage().close();
		createStage();
		showDatabaseScreen();
	}
	private void openAdminUserScreen() {
		getPrimaryStage().close();
		createStage();
		ShowAdminUserScreen();
	}
	
	private void openServerConfigScreen() {
		getPrimaryStage().close();
		createStage();
		showServerConfigScreen();
	}
	private void openCompleteScreen() {
		getPrimaryStage().close();
		createStage();
		showCompleteScreen();
	}

	private EventHandler<WindowEvent> confirmCloseEventHandler = event -> {
		Alert closeConfirmation = new Alert(Alert.AlertType.CONFIRMATION,
				"Do you wish to exit the installation wizard?");
		Button exitButton = (Button) closeConfirmation.getDialogPane().lookupButton(ButtonType.OK);
		exitButton.setText("Ok");

		Button cancelButton = (Button) closeConfirmation.getDialogPane().lookupButton(ButtonType.CANCEL);
		cancelButton.setText("Cancel");
		closeConfirmation.setHeaderText("Confirm exit");
		closeConfirmation.initModality(Modality.APPLICATION_MODAL);
		closeConfirmation.initOwner(getPrimaryStage());

		Optional<ButtonType> closeResponse = closeConfirmation.showAndWait();
		if (!ButtonType.OK.equals(closeResponse.get())) {
			event.consume();
		}else {
			ConfigurationProperties.getInstance().stroreProperties();
		}
	};

	private void createStage() {
		if (this.primaryStage != null) {
			this.primaryStage.close();
		}
		this.primaryStage = new Stage();
		this.primaryStage.setTitle("Connect System Installer");
		this.primaryStage.getIcons().add(new Image("resources/Packager_Install_Icon.png"));
		initRootLayout();

	}

	private void successAlert() {
		try {
		SaveToMasterScript saveToMasterScript =  new SaveToMasterScript();
		saveYamlProperties.saveDataToYaml();
		saveToMasterScript.saveDataToSQL();
		SaveToExcelFile excelFile=new SaveToExcelFile();
		Map< String, String> input=new HashMap<>();
		input.put("firstName", configPropMap.getProperty("adminFirstName"));
		input.put("lastName", configPropMap.getProperty("adminLastName"));
		input.put("email", configPropMap.getProperty("adminEmail"));
		input.put("loginName", configPropMap.getProperty("adminUsername"));
		input.put("password", configPropMap.getProperty("adminPassword"));
		
		excelFile.saveToExcel(input);
		getPrimaryStage().close();
		new File(saveYamlProperties.getFilepath("propertypath")).delete();
		Alert alert = new Alert(AlertType.NONE, "Connect software has been installed successfully");
		alert.show();
		Timeline idlestage = new Timeline(new KeyFrame(Duration.seconds(5), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Platform.exit();
			}
		}));
		idlestage.setCycleCount(1);
		idlestage.play();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void dbValidation() throws Exception {
		BasicDataSource basicDataSource=null;
		basicDataSource = new BasicDataSource();
		basicDataSource.setDriverClassName("org.postgresql.Driver");
		basicDataSource.setUrl("jdbc:postgresql://"+configPropMap.getProperty("dbAddress","")+":"+configPropMap.getProperty("dbPort","")+"/postgres");
		basicDataSource.setUsername(configPropMap.getProperty("dbUsername",""));
		basicDataSource.setPassword(configPropMap.getProperty("dbPassword",""));
		try {
			basicDataSource.getConnection();
		} catch (Exception e) {
			throw new Exception("Connection Failed! Check output console"); 
		}

	}
	
	public boolean IPValidation(String ipAdress) {
		InetAddressValidator validator = InetAddressValidator.getInstance();
		DomainValidator domainValidator = DomainValidator.getInstance(true);
		if((ipAdress.trim()).equals("localhost") || validator.isValid(ipAdress) || domainValidator.isValid(ipAdress))
			return true;
		else
			return false;
	}
	
	
	public void confirmSavingDetails() {
		Alert saveConfirmation = new Alert(Alert.AlertType.CONFIRMATION,
				"The current settings will be overwritten. Do you want to continue? ");
		Button exitButton = (Button) saveConfirmation.getDialogPane().lookupButton(ButtonType.OK);
		exitButton.setText("Ok");

		Button cancelButton = (Button) saveConfirmation.getDialogPane().lookupButton(ButtonType.CANCEL);
		cancelButton.setText("Cancel");
		saveConfirmation.setHeaderText("Confirm");
		saveConfirmation.initModality(Modality.APPLICATION_MODAL);
		saveConfirmation.initOwner(getPrimaryStage());
		Optional<ButtonType> saveResponse = saveConfirmation.showAndWait();
		if (ButtonType.OK.equals(saveResponse.get())) {
			successAlert();
		}else {
			saveConfirmation.close();
		}
	}

	
	/**
	 * Returns the main stage.
	 * 
	 * @return
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}

	public void ShowAdminUserScreen() {
		
		try {
			// Load person overview.
						FXMLLoader loader1 = new FXMLLoader();
						loader1.setLocation(AppInstallerMainApp.class.getResource("view/AdminUserSettings.fxml"));
						Pane personOverview = (Pane) loader1.load();
						getPrimaryStage().setOnCloseRequest(confirmCloseEventHandler);
						ValidationUtil util=new ValidationUtil();
						// Set person overview into the center of root layout.
						rootLayout.setCenter(personOverview);
						TextField firstName =((TextField) ((Pane) personOverview.getChildren().get(0)).getChildren().get(4));
						TextField lastName =((TextField) ((Pane) personOverview.getChildren().get(0)).getChildren().get(6));
						TextField  adminEmail=((TextField) ((Pane) personOverview.getChildren().get(0)).getChildren().get(8));
						TextField  userName=((TextField) ((Pane) personOverview.getChildren().get(0)).getChildren().get(10));
						TextField password =((TextField) ((Pane) personOverview.getChildren().get(0)).getChildren().get(12));
						TextField conformPassword  =((TextField) ((Pane) personOverview.getChildren().get(0)).getChildren().get(14));
					
						firstName.setText(configPropMap.getProperty("adminFirstName",""));
						lastName.setText(configPropMap.getProperty("adminLastName",""));
						adminEmail.setText(configPropMap.getProperty("adminEmail",""));
						userName.setText(configPropMap.getProperty("adminUsername",""));
						if(!util.isEmpty(firstName.getText()))
							isFirstName=true;
						if(!util.isEmpty(lastName.getText()))
							isLastName=true;
						if(!util.isEmpty(adminEmail.getText()))
							isAdminEmail=true;
						if(!util.isEmpty(userName.getText()))
							isUserName=true;
						enableNextForAdminUsesr(personOverview);
						
						
						firstName.focusedProperty().addListener(new ChangeListener<Boolean>() {
							
							@Override
							public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
			
								if ((!newValue)) {
			isFirstName = false;
			enableNextForAdminUsesr(personOverview);
			if (util.isEmpty(firstName.getText())) {
				((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(17))
						.setText("Please enter the First name.");
				((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(17)).setVisible(true);
			} else if (!util.isEmpty(firstName.getText())
					&& (firstName.getText().length() - firstName.getText().replaceAll(" ", "").length()) > 1) {
				((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(17))
						.setText("Repeated spaces are not allowed in this field.");
				((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(17)).setVisible(true);
			} else if (!util.isValidName(firstName.getText())) {
				((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(17))
						.setText("Allowed characters are alphabets, hyphen, apostrophe and one space.");
				((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(17)).setVisible(true);
			} else if (firstName.getText().length() < 3) {
				((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(17))
						.setText("Minimum field length is 3 characters.");
				((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(17)).setVisible(true);
			} else if (firstName.getText().length() > 30) {
				((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(17))
						.setText("Allowed maximum field length is 30 characters.");
				((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(17)).setVisible(true);
			} else {
				isFirstName = true;
				((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(17)).setVisible(false);
				enableNextForAdminUsesr(personOverview);
			}
		}}});

						lastName.focusedProperty().addListener(new ChangeListener<Boolean>() {
							
							@Override
							public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
			
								if ((!newValue)) {
			isLastName = false;
			enableNextForAdminUsesr(personOverview);
			if (util.isEmpty(lastName.getText())) {
				((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(18))
						.setText("Please enter the Last name.");
				((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(18)).setVisible(true);
			} else if (!util.isValidName(lastName.getText())) {
				((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(18))
						.setText("Allowed characters are alphabets, hyphen, apostrophe and one space.");
				((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(18)).setVisible(true);
			} else if (lastName.getText().length() < 3) {
				((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(18))
						.setText("Minimum field length is 3 characters.");
				((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(18)).setVisible(true);
			} else if (!util.isEmpty(lastName.getText())
					&& (lastName.getText().length() - lastName.getText().replaceAll(" ", "").length()) > 1) {
				((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(18))
						.setText("Repeated spaces are not allowed in this field.");
				((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(18)).setVisible(true);

			} else if (lastName.getText().length() > 30) {
				((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(18))
						.setText("Allowed maximum field length is 30 characters.");
				((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(18)).setVisible(true);
			} else {
				isLastName = true;
				enableNextForAdminUsesr(personOverview);

				((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(18)).setVisible(false);

			}

		}}});

		adminEmail.focusedProperty().addListener(new ChangeListener<Boolean>() {
			
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

				if ((!newValue)) {
			isAdminEmail = false;
			enableNextForAdminUsesr(personOverview);
			if (util.isEmpty(adminEmail.getText())) {
				((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(19))
						.setText("Please enter the Email address.");
				((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(19)).setVisible(true);
			} else if (!util.isValidEmail(adminEmail.getText())) {
				((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(19))
						.setText("Please enter a valid Email address.");
				((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(19)).setVisible(true);
			} else if (!util.isEmpty(adminEmail.getText()) && adminEmail.getText().contains("@")) {
				if (adminEmail.getText().split("@")[0].length() > 64) {
					((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(19))
							.setText("The Email address name should consist of only 64 characters.");
					((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(19)).setVisible(true);
				} else if (adminEmail.getText().split("@")[1].length() > 225) {
					((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(19))
							.setText("The domain name should consist of only 255 characters.");
					((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(19)).setVisible(true);
				} else {
					isAdminEmail = true;
					((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(19)).setVisible(false);
				}
			} else {
				isAdminEmail = true;
				enableNextForAdminUsesr(personOverview);
				((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(19)).setVisible(false);
			}
		}}});

		
		userName.textProperty().addListener((ov, oldValue, newValue) -> {
			userName.setText(newValue.toLowerCase());});
		userName.focusedProperty().addListener(new ChangeListener<Boolean>() {
			
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

				if ((!newValue)) {					
			isUserName = false;
			enableNextForAdminUsesr(personOverview);
			if (util.isEmpty(userName.getText())) {
				((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(20))
						.setText("Please enter the Username.");
				((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(20)).setVisible(true);
			} else if (!util.iscontainoneCha(userName.getText()) && util.isValidUserName(userName.getText())) {
				((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(20))
						.setText("Atleast one alphabet required.");
				((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(20)).setVisible(true);
			} else if ((userName.getText().length() < 3) && (util.isValidUserName(userName.getText()))) {
				((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(20))
						.setText("Minimum field length is 3 characters.");
				((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(20)).setVisible(true);
			} else if (!util.isEmpty(userName.getText())
					&& (userName.getText().length() - userName.getText().replaceAll(" ", "").length()) > 1) {
				((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(20))
						.setText("Repeated spaces are not allowed in this field.");
				((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(20)).setVisible(true);
			} else if (userName.getText().length() > 20) {
				((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(20))
						.setText("Allowed maximum field length is 20 characters.");
				((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(20)).setVisible(true);
			} else if (!util.isValidUserName(userName.getText())) {
				((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(20))
						.setText("Allowed special characters are underscore and dot within the username.");
				((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(20)).setVisible(true);
			} else {
				isUserName = true;
				((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(20)).setVisible(false);
			}
			enableNextForAdminUsesr(personOverview);
		}}});
		
		
		password.focusedProperty().addListener(new ChangeListener<Boolean>() {
			
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

				if ((!newValue)) {
			isPassword = false;
		
			if ((!util.isEmpty(password.getText())) && util.checkMinLenth(password.getText(), 8)
					&& util.checkMaxLength(password.getText(), 25) && util.isValidPassword(password.getText())
					&& !util.longestSubstring(userName.getText(), password.getText())
					&& !util.isrepatecharmorethanFourtime(password.getText())) {

				if ((!util.isEmpty(conformPassword.getText()))) {
					if (util.ismacthpassword(password.getText(), conformPassword.getText())) {
						isPassword = true;
						((TextArea) ((Pane) personOverview.getChildren().get(0)).getChildren().get(16))
								.setVisible(false);
						((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(21))
						.setVisible(false);
					} else {
						isPassword = false;
						((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(21))
								.setText("Mismatch password");
						((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(21))
								.setVisible(true);
					}

				} else {
					isPassword = true;
					((TextArea) ((Pane) personOverview.getChildren().get(0)).getChildren().get(16))
							.setVisible(false);
				}
			} else {
				((TextArea) ((Pane) personOverview.getChildren().get(0)).getChildren().get(16))
						.setText("Minimum password length is 8 characters.\r\n"
								+ "Maximum password length is 25 characters.\r\n"
								+ "Must contain at least 1 uppercase letter, 1 lowercase letter, 1 number and 1 special character.\r\n"
								+ "Must not repeat a character more than 4 times consecutively. ");
				((TextArea) ((Pane) personOverview.getChildren().get(0)).getChildren().get(16)).setVisible(true);
				((TextArea) ((Pane) personOverview.getChildren().get(0)).getChildren().get(16)).setEditable(false);
			}
			enableNextForAdminUsesr(personOverview);
		}}});
		

		
		conformPassword.focusedProperty().addListener(new ChangeListener<Boolean>() {
			
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

				if ((!newValue)) {
					
			isConformPassword = false;
		
			if (util.isEmpty(conformPassword.getText())) {
				((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(21)).setVisible(true);
				((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(21))
						.setText("Please enter the Confirm password.");
			} else if (util.isEmpty(conformPassword.getText()) && conformPassword.getText().length() < 6) {
				((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(21)).setVisible(true);
				((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(21))
						.setText("Allowed minimum field length is 6 characters.");

			} else if (util.isEmpty(conformPassword.getText()) && conformPassword.getText().length() > 25) {
				((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(21)).setVisible(true);
				((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(21))
						.setText("Allowed maximum field length is 25 characters.");
			} else if (!util.ismacthpassword(password.getText(), conformPassword.getText())) {
				((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(21))
						.setText("Mismatch password");
				((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(21)).setVisible(true);
			} else {
				isConformPassword = true;
				isPassword = true;
				enableNextForAdminUsesr(personOverview);
				((Text) ((Pane) personOverview.getChildren().get(0)).getChildren().get(21)).setVisible(false);
				((TextArea) ((Pane) personOverview.getChildren().get(0)).getChildren().get(16)).setVisible(false);
			}
			enableNextForAdminUsesr(personOverview);
		}}});
		((Button) ((HBox) ((Pane) personOverview.getChildren().get(0)).getChildren().get(0)).getChildren().get(0))
				.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {
						getPrimaryStage().close();
						openLanguageSettings();
					}
				});
			((Button) ((HBox) ((Pane) personOverview.getChildren().get(0)).getChildren().get(0)).getChildren().get(1))
			.setOnAction(new EventHandler<ActionEvent>() {	
				@Override
					public void handle(ActionEvent arg0) {
						configPropMap.setProperty("adminFirstName", firstName.getText());
						configPropMap.setProperty("adminLastName", lastName.getText());
						configPropMap.setProperty("adminUsername", userName.getText());
						configPropMap.setProperty("adminEmail", adminEmail.getText());
						configPropMap.setProperty("adminPassword", password.getText());
						configPropMap.setProperty("adminConfirmPassword", conformPassword.getText());
						getPrimaryStage().close();
						openSMTPSettings();
					}
			});			
						((Button) ((HBox) ((Pane) personOverview.getChildren().get(0)).getChildren().get(0)).getChildren().get(2))
								.setOnAction(event -> getPrimaryStage()
										.fireEvent(new WindowEvent(getPrimaryStage(), WindowEvent.WINDOW_CLOSE_REQUEST)));
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
