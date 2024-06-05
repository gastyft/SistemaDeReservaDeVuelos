package org.SistemaVuelos.menus;

import org.SistemaVuelos.exceptions.VueloNoEncontradoException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

public class MenuPrincipal { //todo Llamadas a los sub menus
        public void iniciar()  {

              Scanner scanner = new Scanner(System.in);
              //Inicializacion de menus
              MenuVuelos menuVuelos = new MenuVuelos();
              MenuPasajeros menuPasajeros = new MenuPasajeros();
              MenuReservas menuReservas = new MenuReservas(menuPasajeros.gestorPasajeros, menuVuelos.gestorVuelos);

              System.out.println("BIENVENIDO");     //String de menu con opciones
              String menu = """
                      ---------------------MENU PRINCIPAL-------------------
                      1-Gestion de Vuelos
                      2-Gestion de Pasajeros
                      3-Gestion de Reservas
                      4-Contactar al desarrollador
                      ESCRIBA "ESC" PARA SALIR DEL PROGRAMA
                      """;

              String opc = "Esc";
              do {
                  System.out.println(menu);
                  opc = scanner.nextLine().toUpperCase();

                  switch (opc) { //Llamada a los distintos menus con sus gestores
                      case "1" -> menuVuelos.menuVuelos(menuReservas.gestorReservas);
                      case "2" -> menuPasajeros.menuPasajeros();
                      case "3" -> menuReservas.menuReservas();
                      case "4" -> mostrarInformacionContacto(); //Funcion Extra
                      case "ESC" -> System.out.println("ESCRIBIO ESCAPE");
                      default -> System.out.println("Opcion incorrecta. Seleccione nuevamente");
                  }

              } while (!opc.equalsIgnoreCase("Esc"));

              System.out.println("Salio del sistema. Gracias");

        }
    private void mostrarInformacionContacto() { //Funcion extra con Swing
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Contactar al Desarrollador");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(400, 200);

            JPanel panel = new JPanel();
            JLabel label = new JLabel("Seleccione un enlace para abrir en el navegador:");
            JButton whatsappButton = new JButton("WhatsApp");
            JButton portfolioButton = new JButton("Portfolio");

            whatsappButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    abrirEnlaceEnNavegador("https://wa.me/2235034695");
                }
            });

            portfolioButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    abrirEnlaceEnNavegador("https://portfolio-yo-programo.web.app/principal");
                }
            });

            panel.add(label);
            panel.add(whatsappButton);
            panel.add(portfolioButton);
            frame.add(panel);

            frame.setVisible(true);
        });
    }

    private void abrirEnlaceEnNavegador(String url) { //Abridor de enlaces al navegador desde un Button
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop desktop = Desktop.getDesktop();
                desktop.browse(new URI(url));
            } catch (IOException | URISyntaxException e) {
                System.out.println("No se pudo abrir el enlace en el navegador: " + e.getMessage());
            }
        } else {
            System.out.println("La función para abrir el navegador no es compatible en este sistema.");
        }
    }


}
