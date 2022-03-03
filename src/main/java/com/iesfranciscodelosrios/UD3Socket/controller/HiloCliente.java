package com.iesfranciscodelosrios.UD3Socket.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import Model.Cuenta;
import Model.Mensaje;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.stage.Stage;

public class HiloCliente implements Runnable {
	private boolean timeToStop = false;
	private ObjectInputStream in;
	private Mensaje mensaje;

	public HiloCliente(Mensaje mensaje, ObjectInputStream in) throws IOException {
		this.mensaje = mensaje;
		this.in = in;
	}

	public void stop() {
		timeToStop = true;
	}

	public void run() {
		while (!timeToStop) {
			try {

				mensaje = (Mensaje) in.readObject();
				
				cliente(!mensaje.getUser().getAdministrador());
				
				login(mensaje.getDescripcion());
				
				

			} catch (IOException e) {
				if ("Connection reset".equals(e.getMessage())) {
					System.out.println("�La conexi�n al servidor ha sido interrumpida!");
					break;
				}
				if (!timeToStop) {
					e.printStackTrace();
				}
			} catch (ClassNotFoundException e) {	
			} catch (Exception e) {
				System.out.println("Error hilo cliente");
			}
		}

	}
	
	private void cliente(boolean b) {
		Platform.runLater(() -> {
			try {
				// MENU DE ACCIONES PARA LOS CLIENTES.
				if (!mensaje.getUser().getAdministrador()) {
					switch (mensaje.getComando()) {
					case 100:
						if (mensaje.getCuenta()!=null) {
							ControllerClientes.cuenta = mensaje.getCuenta();	
							ControllerClientes.mensaje = mensaje;
							ControllerClientes.cofigureOnServerResponse();
						}
						break;
						
					default:break;
					}
				} else {
					switch (mensaje.getComando()) {
					case 100:
						if (mensaje.getCuenta()!=null) {
							ControllerClientes.cuenta = mensaje.getCuenta();
							ControllerClientes.mensaje = mensaje;
							ControllerClientes.cofigureOnServerResponse();
						}
						break;
						
					default:break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
	}

	//CONEXION CON EL LOGIN
	public void login(String mesagge) {
		
		switch (mesagge) {
		case "Usuario no registrado":
		case "Usuario ya conectado":
		case "Error al logear.":
			String m = mensaje.getDescripcion();
			MainController.showWarning(m);
			System.out.println(m);
			break;
		// EN CASO DE INICIAR SESION BIEN MUESTRA LA PANTALLA CON LOS DATOS.
		case "logeado":
			MainController.mensaje = mensaje;
			MainController.usuario = mensaje.getUser();
			MainController.showView();
			break;
			
		default:break;
		}
	}

}
