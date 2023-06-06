package procesosbloqueo;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Multicolas extends JFrame implements Runnable, ActionListener {

    JScrollPane scrollPane = new JScrollPane();
    JScrollPane scrollPane1 = new JScrollPane();

    JScrollPane scrollPane2 = new JScrollPane();
    JScrollPane scrollPane3 = new JScrollPane();
    //Tabla Bloquedos
    JScrollPane scrollPane4 = new JScrollPane();
    JScrollPane scrollPane5 = new JScrollPane();
    //Tabla RR
    JScrollPane scrollPane6 = new JScrollPane();
    JScrollPane scrollPane7 = new JScrollPane();
    //Tabla SRTF
    JScrollPane scrollPane8 = new JScrollPane();
    JScrollPane scrollPane9 = new JScrollPane();
    //Tabla FCFS
    JScrollPane scrollPane10 = new JScrollPane();
    JScrollPane scrollPane11 = new JScrollPane();

    JLabel label1 = new JLabel("Nombre del proceso: ");
    JLabel label3 = new JLabel("Proceso en ejecucion: Ninguno");
    JLabel label4 = new JLabel("Tiempo: ");
    JLabel label5 = new JLabel("Tabla de procesos:");
    JLabel label6 = new JLabel("Diagrama de Gant:");
    JLabel label7 = new JLabel("Tabla de Bloqueados:");
    JLabel label8 = new JLabel("Rafaga restante del proceso: 0");
    JLabel label9 = new JLabel("Rafaga:");
    JLabel label10 = new JLabel("RoundRobin");
    JLabel label11 = new JLabel("SRTF");
    JLabel label12 = new JLabel("FCFS");
    JLabel label13 = new JLabel("Tiempo de envejecimiento: ");

    JButton botonIngresar = new JButton("Ingresar proceso");
    JButton botonIniciar = new JButton("Iniciar ejecucion");
    JButton botonbloquear = new JButton("Bloquear");

    JComboBox eleccion = new JComboBox();
    JTextField tfNombre = new JTextField("P1");
    JTextField tfrafaga = new JTextField("");

    JTextField[][] tabla = new JTextField[100][8];
    JTextField[][] tablaProcesos = new JTextField[100][3];
    JTextField[][] tablaBloqueados = new JTextField[100][3];
    
    JLabel[][] diagrama = new JLabel[40][100];
    
    ListaCircular colaEjecutada = new ListaCircular();

    ListaCircular cola = new ListaCircular();
    ListaCircular cola2 = new ListaCircular();
    ListaCircular cola3 = new ListaCircular();
   
    
    Nodo nodoEjecutado;

    int filas = 0, rafagaTemporal;
    int tiempoGlobal = 0;
    int coorX = 0, coorY = 1,indice = 0;
    boolean orden = false;

    Thread procesos;

    public static void main(String[] args) throws InterruptedException {

        Multicolas pb = new Multicolas();
      
        pb.setExtendedState(pb.MAXIMIZED_BOTH);
        pb.setTitle("Proyecto final: sistemas operativos  Sebastian Morales - 20182020039  Diego Perez - 2022102073");
        pb.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pb.setVisible(true);
        

    }

    Multicolas() {

        Container c = getContentPane();
        c.setLayout(null);
        this.getContentPane().setBackground(Color.orange);
        c.add(label1);
        c.add(label3);
        c.add(label4);
        c.add(label5);
        c.add(label6);
        c.add(label7);
        c.add(label8);
        c.add(label9);
        c.add(label10);
        c.add(label11);
        c.add(label12);
        c.add(label13);
        
        c.add(tfrafaga);
        c.add(scrollPane1);
        c.add(scrollPane3);
        c.add(scrollPane5);
        c.add(scrollPane7);
        c.add(scrollPane9);
        c.add(scrollPane11);
        
        c.add(eleccion);
        
        eleccion.addItem("Round robin");
        eleccion.addItem("SRTF");
        eleccion.addItem("FCFS");

        c.add(botonIngresar);
        c.add(botonIniciar);
        c.add(botonbloquear);

        c.add(tfNombre);

        label1.setBounds(10, 650, 300, 20);
        label3.setBounds(730, 665, 300, 20);
        label4.setBounds(1110, 665, 300, 20);
        label5.setBounds(10, 10, 300, 20);
        label6.setBounds(10, 270, 300, 20);
        label7.setBounds(1020, 340, 300, 20);
        label8.setBounds(920, 665, 300, 20);
        label9.setBounds(10, 673, 300, 20);
        label10.setBounds(730, 10, 300, 20);
        label11.setBounds(1020, 10, 300, 20);
        label12.setBounds(730, 340, 300, 20);
        label13.setBounds(730, 685,300, 20);
        eleccion.setBounds(110, 676, 100, 20);
        //Tabla procesos
        scrollPane.setBounds(10, 30, 2500, 2500);
        scrollPane.setPreferredSize(new Dimension(2500, 2500));
        scrollPane.setBackground(Color.LIGHT_GRAY);

        scrollPane1.setBounds(10, 30, 700, 230);
        scrollPane1.setPreferredSize(new Dimension(1150, 400));
        scrollPane1.setBackground(Color.LIGHT_GRAY);
        //Diagrama grantt
        scrollPane2.setBounds(10, 288, 2500, 2500);
        scrollPane2.setPreferredSize(new Dimension(2500, 2500));
        scrollPane2.setBackground(Color.LIGHT_GRAY);

        scrollPane3.setBounds(10, 288, 700, 350);
        scrollPane3.setPreferredSize(new Dimension(1150, 400));
        scrollPane3.setBackground(Color.WHITE);

        scrollPane2.setBounds(10, 288, 2500, 2500);
        scrollPane2.setPreferredSize(new Dimension(2500, 2500));
        scrollPane2.setBackground(Color.WHITE);

        scrollPane3.setBounds(10, 288, 700, 350);
        scrollPane3.setPreferredSize(new Dimension(700, 350));
        scrollPane3.setBackground(Color.WHITE);
        //Tabla bloquados
        scrollPane4.setBounds(1020, 360, 500, 1000);
        scrollPane4.setPreferredSize(new Dimension(500, 1000));
        scrollPane4.setBackground(Color.WHITE);

        scrollPane5.setBounds(1020, 360, 260, 300);
        scrollPane5.setPreferredSize(new Dimension(260, 300));
        scrollPane5.setBackground(Color.WHITE);
        //Tabla roundRobin
        scrollPane6.setBounds(730, 30, 500, 1000);
        scrollPane6.setPreferredSize(new Dimension(500, 1000));
        scrollPane6.setBackground(Color.WHITE);

        scrollPane7.setBounds(730, 30, 260, 300);
        scrollPane7.setPreferredSize(new Dimension(260, 300));
        scrollPane7.setBackground(Color.WHITE);
        //Tabla SRTF
        scrollPane8.setBounds(1020, 30, 500, 1000);
        scrollPane8.setPreferredSize(new Dimension(500, 1000));
        scrollPane8.setBackground(Color.WHITE);

        scrollPane9.setBounds(1020, 30, 260, 300);
        scrollPane9.setPreferredSize(new Dimension(260, 300));
        scrollPane9.setBackground(Color.WHITE);
        //Tabla FCFS
        scrollPane10.setBounds(730, 360, 500, 1000);
        scrollPane10.setPreferredSize(new Dimension(500, 1000));
        scrollPane10.setBackground(Color.WHITE);

        scrollPane11.setBounds(730, 360, 260, 300);
        scrollPane11.setPreferredSize(new Dimension(260, 300));
        scrollPane11.setBackground(Color.WHITE);

        tfNombre.setBounds(135, 650, 70, 20);
        tfNombre.setEditable(false);
        tfrafaga.setBounds(60, 676,45 , 20);

        botonIngresar.addActionListener(this);
        botonIngresar.setBounds(220, 650, 150, 40);
        botonIngresar.setBackground(Color.LIGHT_GRAY);

        botonIniciar.addActionListener(this);
        botonIniciar.setBounds(390, 650, 150, 40);
        botonIniciar.setBackground(Color.LIGHT_GRAY);
        
      /*  botonbloquear.addActionListener(this);
        botonbloquear.setBounds(560, 650, 150, 40);
        botonbloquear.setBackground(Color.LIGHT_GRAY);*/

        colaEjecutada = cola2;
 
    }

    public void dibujarTabla(String nombre, int rafaga, int tiempo) {

        scrollPane.removeAll();

        JLabel texto1 = new JLabel("Proceso");
        JLabel texto2 = new JLabel("T. llegada");
        JLabel texto3 = new JLabel("Rafaga");
        JLabel texto5 = new JLabel("T. comienzo");
        JLabel texto6 = new JLabel("T. final");
        JLabel texto7 = new JLabel("T. retorno");
        JLabel texto8 = new JLabel("T. espera");
        JLabel texto9 = new JLabel("Estado");

        texto1.setBounds(20, 20, 150, 20);
        texto2.setBounds(100, 20, 150, 20);
        texto3.setBounds(180, 20, 150, 20);
        texto5.setBounds(260, 20, 150, 20);
        texto6.setBounds(340, 20, 150, 20);
        texto7.setBounds(420, 20, 150, 20);
        texto8.setBounds(500, 20, 150, 20);
        texto9.setBounds(580, 20, 150, 20);
     

        scrollPane.add(texto1);
        scrollPane.add(texto2);
        scrollPane.add(texto3);
        scrollPane.add(texto5);
        scrollPane.add(texto6);
        scrollPane.add(texto7);
        scrollPane.add(texto8);
        scrollPane.add(texto9);

        for (int i = 0; i < filas; i++) {

            for (int k = 0; k < 8; k++) {

                if (tabla[i][k] != null) {
                    scrollPane.add(tabla[i][k]);
                } else {

                    tabla[i][k] = new JTextField("-");
                    tabla[i][k].setBounds(20 + (k * 80), 40 + (i * 25), 70, 20);

                    if (k == 7) {
                        tabla[i][k] = new JTextField("Espera");
                        tabla[i][k].setBounds(20 + (k * 80), 40 + (i * 25), 70, 20);
                    }
                    scrollPane.add(tabla[i][k]);
                }

            }

        }

        tabla[filas - 1][0].setText(nombre);
        tabla[filas - 1][1].setText(Integer.toString(tiempo));
        tabla[filas - 1][2].setText(Integer.toString(rafaga));

        scrollPane.repaint();
        scrollPane1.setViewportView(scrollPane);

    }
    public void dibujarTablaEjecucion(ListaCircular cola, JScrollPane scrollPar, JScrollPane scrollImpar){
        
        scrollPar.removeAll();
        
        JLabel texto1 = new JLabel("Proceso");
        JLabel texto2 = new JLabel("Rafaga");
        JLabel texto3 = new JLabel("T. Llegada");
        
        texto1.setBounds(20, 20, 150, 20);
        texto2.setBounds(100, 20, 150, 20);
        texto3.setBounds(180, 20, 150, 20);
        
        scrollPar.add(texto1);
        scrollPar.add(texto2);
        scrollPar.add(texto3);
        
        if(cola.getCabeza() != null){
        
        Nodo temp = cola.getCabeza();
            
            for(int i = 0; i<cola.getTamaño(); i++){

                for(int j = 0; j<3 ; j++){

                        tablaProcesos[i][j] = new JTextField("");
                        tablaProcesos[i][j].setBounds(20 + (j*80), 40 + (i*25), 70, 20);

                        scrollPar.add(tablaProcesos[i][j]);

                }

                tablaProcesos[i][0].setText(temp.getLlave());
                tablaProcesos[i][1].setText(Integer.toString(temp.getRafaga()));
                tablaProcesos[i][2].setText(Integer.toString(temp.getLlegada()));
                
                temp = temp.getSiguiente();

            }
        }
        
        scrollPar.repaint();
        scrollImpar.setViewportView(scrollPar);
        
    }
    public void llenarBloqueados(ListaCircular colaBloquear) {

        scrollPane4.removeAll();

        JLabel texto1 = new JLabel("Proceso");
        JLabel texto2 = new JLabel("T. llegada");
        JLabel texto3 = new JLabel("Rafaga");

        texto1.setBounds(20, 20, 150, 20);
        texto2.setBounds(100, 20, 150, 20);
        texto3.setBounds(180, 20, 150, 20);


        scrollPane4.add(texto1);
        scrollPane4.add(texto2);
        scrollPane4.add(texto3);
  

        if (colaBloquear.getCabeza() != null) {
           
            Nodo temp = colaBloquear.getCabeza().getSiguiente();

            for (int i = 0; i < colaBloquear.getTamaño()-1; i++) {

                for (int j = 0; j < 3; j++) {

                    tablaBloqueados[i][j] = new JTextField("");
                    tablaBloqueados[i][j].setBounds(20 + (j * 80), 40 + (i * 25), 70, 20);

                    scrollPane4.add(tablaBloqueados[i][j]);

                }

                tablaBloqueados[i][0].setText(temp.getLlave());
                tablaBloqueados[i][1].setText(Integer.toString(temp.getLlegada()));
                tablaBloqueados[i][2].setText(Integer.toString(temp.getRafaga()));


                temp = temp.getSiguiente();

            }
        }
        
        scrollPane4.repaint();
        scrollPane5.setViewportView(scrollPane4);

    }

    public void llenarRestante() {
        if(colaEjecutada==cola){
          
            if (nodoEjecutado.getRafaga() == 0) {
            tabla[nodoEjecutado.getIndice() - 1][7].setText("Terminado");
        } else {
            tabla[nodoEjecutado.getIndice() - 1][7].setText("Bloqueado");
        }

            tabla[nodoEjecutado.getIndice() - 1][3].setText(Integer.toString(nodoEjecutado.getComienzo()));
            tabla[nodoEjecutado.getIndice() - 1][4].setText(Integer.toString(nodoEjecutado.getFinalizacion()));
            tabla[nodoEjecutado.getIndice() - 1][5].setText(Integer.toString(nodoEjecutado.getFinalizacion() - nodoEjecutado.getLlegada()));
            tabla[nodoEjecutado.getIndice() - 1][6].setText(Integer.toString(nodoEjecutado.getComienzo() - nodoEjecutado.getLlegada()));
        }
        else if(colaEjecutada== cola2){
            if(nodoEjecutado.getRafaga() == 0) {
    		tabla[nodoEjecutado.getIndice()-1][7].setText("Terminado");
            }
            else {
                    tabla[nodoEjecutado.getIndice()-1][7].setText("Bloqueado");
            }

            tabla[nodoEjecutado.getIndice()-1][3].setText(tabla[nodoEjecutado.getIndice()-1][3].getText() + "," + Integer.toString(nodoEjecutado.getComienzo()));
            tabla[nodoEjecutado.getIndice()-1][4].setText(tabla[nodoEjecutado.getIndice()-1][4].getText() + "," +Integer.toString(nodoEjecutado.getFinalizacion()));
            tabla[nodoEjecutado.getIndice()-1][5].setText(Integer.toString(nodoEjecutado.getFinalizacion() - nodoEjecutado.getLlegada()));

            String [] comienzos = tabla[nodoEjecutado.getIndice()-1][3].getText().split(","); 
            String [] finales = tabla[nodoEjecutado.getIndice()-1][4].getText().split(","); 
            finales[0] = "0";
            String cadena = "";

            for(int i = 1; i<comienzos.length; i++){

                cadena = cadena + (Integer.parseInt(comienzos[i]) - Integer.parseInt(finales[i-1])) + ",";

            }

            tabla[nodoEjecutado.getIndice()-1][6].setText(cadena);
        
        }else if(colaEjecutada==cola3){
             if (nodoEjecutado.getRafaga() == 0) {
            tabla[nodoEjecutado.getIndice() - 1][7].setText("Terminado");
        } else {
            tabla[nodoEjecutado.getIndice() - 1][7].setText("Bloqueado");
        }

            tabla[nodoEjecutado.getIndice() - 1][3].setText(Integer.toString(nodoEjecutado.getComienzo()));
            tabla[nodoEjecutado.getIndice() - 1][4].setText(Integer.toString(nodoEjecutado.getFinalizacion()));
            tabla[nodoEjecutado.getIndice() - 1][5].setText(Integer.toString(nodoEjecutado.getFinalizacion() - nodoEjecutado.getLlegada()));
            tabla[nodoEjecutado.getIndice() - 1][6].setText(Integer.toString(nodoEjecutado.getComienzo() - nodoEjecutado.getLlegada()));
        }
            
        
        

    }

    public void dibujarEsperas() {

        JLabel img2 = new JLabel();

        ImageIcon imgIcon2 = new ImageIcon(getClass().getResource("barraEspera.png"));

        Image imgEscalada2 = imgIcon2.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        Icon iconoEscalado2 = new ImageIcon(imgEscalada2);

        for (int i = coorX - 1; i >= nodoEjecutado.getLlegada(); i--) {

            diagrama[coorY][i + 1] = new JLabel();
            diagrama[coorY][i + 1].setBounds(40 + (i * 20), 20 + (coorY * 20), 20, 20);
            diagrama[coorY][i + 1].setIcon(iconoEscalado2);

            scrollPane2.add(diagrama[coorY][i + 1]);

        }

    }

    public void dibujarDiagrama(String nombre, int coorX, int coorY) {

        scrollPane2.removeAll();
         
        for (int i = 0; i < 100; i++) {

            diagrama[0][i] = new JLabel(Integer.toString(i));
            diagrama[0][i].setBounds(40 + (i * 20), 20, 20, 20);

            scrollPane2.add(diagrama[0][i]);

        }
        if(colaEjecutada == cola2){
            
            diagrama[coorY][0] = new JLabel("  " + nombre);
            diagrama[coorY][0].setBounds(0, 20 + (coorY * 20), 50, 20);
            scrollPane2.add(diagrama[coorY][0]);

                for (int i = 1; i < filas + 1; i++) {
                for (int j = 0; j < coorX + 1; j++) {
                    if (diagrama[i][j] != null) {
                        scrollPane2.add(diagrama[i][j]);
                    }
                }
            }

        }else if(colaEjecutada == cola){
            diagrama[coorY][0] = new JLabel("  " + nombre);
            diagrama[coorY][0].setBounds(0, 20 + (coorY * 20), 50, 20);
            scrollPane2.add(diagrama[coorY][0]);

            for (int i = 1; i < coorY + 1; i++) {
                for (int j = 0; j < coorX + 1; j++) {
                    if (diagrama[i][j] != null) {
                        scrollPane2.add(diagrama[i][j]);
                    }
                }
            }
        }else if(colaEjecutada == cola3){
            
            diagrama[coorY][0] = new JLabel("  " + nombre);
            diagrama[coorY][0].setBounds(0, 20 + (coorY * 20), 50, 20);
            scrollPane2.add(diagrama[coorY][0]);

            for (int i = 1; i < coorY + 1; i++) {
                for (int j = 0; j < coorX + 1; j++) {
                    if (diagrama[i][j] != null) {
                        scrollPane2.add(diagrama[i][j]);
                    }
                }
            }
        
        }
        JLabel img = new JLabel();

        ImageIcon imgIcon = new ImageIcon(getClass().getResource("barra.png"));

        Image imgEscalada = imgIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        Icon iconoEscalado = new ImageIcon(imgEscalada);
        diagrama[coorY][coorX + 1] = new JLabel();
        diagrama[coorY][coorX + 1].setBounds(40 + (coorX * 20), 20 + (coorY * 20), 20, 20);
        diagrama[coorY][coorX + 1].setIcon(iconoEscalado);
        scrollPane2.add(diagrama[coorY][coorX + 1]);
        
        scrollPane2.repaint();
        scrollPane3.setViewportView(scrollPane2);

    }

   
    public void ingresar(String nombre,int rafaga, int tiempo, int filas, boolean ordenar) {
        String eleccion2 = String.valueOf(eleccion.getSelectedItem());
        if(eleccion2 == "SRTF"){
            ordenar = true;
        
            cola.insertar(nombre, rafaga, tiempo, filas, ordenar);
        }else if(eleccion2 == "Round robin" ){
            ordenar = false;
            cola2.insertar(nombre, rafaga, tiempo, filas,ordenar);
           
        }else if(eleccion2 == "FCFS"){
            ordenar = false;
      
            cola3.insertar(nombre, rafaga, tiempo, filas,ordenar);
            
        }


    }
    @Override
    public void actionPerformed(ActionEvent e) {
        
        if (e.getSource() == botonIngresar) {
      
            String eleccion1 = String.valueOf(eleccion.getSelectedItem());
            String nombre = tfNombre.getText();
            String valorraf = tfrafaga.getText();
                
            try {
               int varafa = Integer.parseInt(valorraf);
               rafagaTemporal = varafa;
               if(rafagaTemporal>0){
                    filas++;
                    if(eleccion1 == "SRTF"){
                        ingresar(nombre, rafagaTemporal, tiempoGlobal, filas,orden);
                        dibujarTabla(nombre,  rafagaTemporal, tiempoGlobal);    
                        dibujarTablaEjecucion(cola, scrollPane8, scrollPane9);
                        llenarBloqueados(colaEjecutada);
                        tfNombre.setText("P" + (filas + 1));

                    }else if(eleccion1 == "Round robin"){
                        ingresar(nombre, rafagaTemporal, tiempoGlobal, filas,orden);
                        dibujarTabla(nombre,  rafagaTemporal, tiempoGlobal);    
                        dibujarTablaEjecucion(cola2, scrollPane6, scrollPane7);
                        tfNombre.setText("P" + (filas + 1));
                        if(colaEjecutada.equals(cola2)){
                            coorY++;
                        }
                        
                    }else if(eleccion1 == "FCFS"){
                        ingresar(nombre, rafagaTemporal, tiempoGlobal, filas,orden);
                        dibujarTabla(nombre,  rafagaTemporal, tiempoGlobal);    
                        dibujarTablaEjecucion(cola3, scrollPane10, scrollPane11);
                        tfNombre.setText("P" + (filas + 1));
               
                    }
                            
                    
                }   
                
            } catch (Exception e2) {
                JOptionPane.showMessageDialog(rootPane, "ERROR: Debe ingresar el valor para la rafaga (Mayor a 0) ");
            }
            if(eleccion1 == "SRTF"){
                
                try {
                if(rafagaTemporal<nodoEjecutado.getRafaga()){
                    
                        if (nodoEjecutado.getRafaga() != 0) {
                           filas++;
                            ingresar(nodoEjecutado.getLlave() + "*", nodoEjecutado.getRafaga(), tiempoGlobal, filas, orden);
                            dibujarTabla(nodoEjecutado.getLlave() + "*", nodoEjecutado.getRafaga(), tiempoGlobal);
                            nodoEjecutado.setFinalizacion(tiempoGlobal);
                            llenarRestante();
                            colaEjecutada.eliminar(nodoEjecutado);
                            dibujarTablaEjecucion(cola, scrollPane8, scrollPane9);
                            llenarBloqueados(colaEjecutada);
                            nodoEjecutado = colaEjecutada.getCabeza();
                            coorY++;
                            nodoEjecutado.setComienzo(tiempoGlobal);
                            dibujarEsperas();
                        }
                }
                } catch (Exception e1) {

                }
            }   
            
            
        } else if (e.getSource() == botonIniciar) {

            procesos = new Thread(this);
            procesos.start();

        }
    }

    @Override
    public void run() {
        do{                
        try {
            if(colaEjecutada.equals(cola)){         
                while (colaEjecutada.getTamaño() != 0) {
                    if (cola2.getTamaño() > 0){
                        colaEjecutada = cola2;
                        break;
                    }else{
                        llenarBloqueados(colaEjecutada);
                        nodoEjecutado = colaEjecutada.getCabeza();
                        nodoEjecutado.setComienzo(tiempoGlobal);
                        dibujarEsperas();      
                        
                    }
                          
                    while (nodoEjecutado.getRafaga() > 0) {
                        tabla[nodoEjecutado.getIndice() - 1][7].setText("Ejecutando");
                        nodoEjecutado.setRafaga(nodoEjecutado.getRafaga() - 1);                   
                        label3.setText("Proceso en ejecucion: " + nodoEjecutado.getLlave());
                        label4.setText("Tiempo: " + String.valueOf(tiempoGlobal) + " Segundos.");
                        label8.setText("Rafaga restante del proceso: " + nodoEjecutado.getRafaga());
                        dibujarDiagrama(nodoEjecutado.getLlave(), coorX, coorY);                  
                        tiempoGlobal++;
                        coorX++;
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Multicolas.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                        if (cola2.getTamaño() > 0) {
                                filas++;
                                cola.insertar(nodoEjecutado.getLlave() + "*", nodoEjecutado.getRafaga(), tiempoGlobal, filas, orden);
                                dibujarTabla(nodoEjecutado.getLlave() + "*", nodoEjecutado.getRafaga(), tiempoGlobal);
                                nodoEjecutado.setFinalizacion(tiempoGlobal);
                                llenarRestante();
                                colaEjecutada.eliminar(nodoEjecutado);
                                dibujarTablaEjecucion(cola, scrollPane8, scrollPane9);
                                llenarBloqueados(colaEjecutada);
                                coorY+=2;
                                colaEjecutada = cola2;
                                
                                // Reiniciar el ciclo para ejecutar el nuevo proceso en cola2
                                break;
                        }
                    }
                    if(cola2.getTamaño()==0){
                        nodoEjecutado.setFinalizacion(tiempoGlobal);
                        llenarRestante();
                        colaEjecutada.eliminar(colaEjecutada.getCabeza());
                        dibujarTablaEjecucion(cola, scrollPane8, scrollPane9);
                        llenarBloqueados(colaEjecutada);
                        coorY++;
                    }

                }
                if (cola2.getTamaño() == 0) {
              
                    cola.desocupar();
                    colaEjecutada.desocupar();
                    colaEjecutada = cola3;
                }
            } if(colaEjecutada.equals(cola2)){
                
                while(colaEjecutada.getTamaño() != 0){ 
                    
                    nodoEjecutado = colaEjecutada.getCabeza();
                    nodoEjecutado.setComienzo(tiempoGlobal);
                    int tiempoEjecutado = 0;
                    while(nodoEjecutado.getRafaga() > 0 && tiempoEjecutado < 4){
                        tabla[nodoEjecutado.getIndice()-1][7].setText("Ejecutando");
                        nodoEjecutado.setRafaga(nodoEjecutado.getRafaga()-1);
                        label3.setText("Proceso en ejecucion: " + nodoEjecutado.getLlave());
                        label4.setText("Tiempo: " + String.valueOf(tiempoGlobal) + " Segundos.");
                        label8.setText("Rafaga restante del proceso: " + nodoEjecutado.getRafaga());
                        dibujarDiagrama(nodoEjecutado.getLlave(), coorX, nodoEjecutado.getIndice());
                        tiempoGlobal++;
                        coorX++;
                        tiempoEjecutado++;
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Multicolas.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                        nodoEjecutado.setFinalizacion(tiempoGlobal);
                        llenarRestante();
                    if(nodoEjecutado.getRafaga() == 0){
                        colaEjecutada.eliminar(colaEjecutada.getCabeza());
                        dibujarTablaEjecucion(cola2, scrollPane6, scrollPane7);
                    } else if (tiempoEjecutado == 4){
                        colaEjecutada.getCabeza().setLlave(colaEjecutada.getCabeza().getLlave());
                        colaEjecutada.intercambiar(colaEjecutada.getCabeza());
                        dibujarTablaEjecucion(cola2, scrollPane6, scrollPane7);
                    }

                }
                cola2.desocupar();
                colaEjecutada.desocupar();
                colaEjecutada = cola; 
            }
            if(colaEjecutada.equals(cola3)){
                while (colaEjecutada.getTamaño() != 0) {
                    if (cola2.getTamaño() > 0){
                        colaEjecutada = cola2;
                        break;
                    }
                    if(cola.getTamaño() > 0){
                        colaEjecutada = cola;
                        break;
                    }else{
                        llenarBloqueados(colaEjecutada);
                        nodoEjecutado = colaEjecutada.getCabeza();
                        nodoEjecutado.setComienzo(tiempoGlobal);
                        dibujarEsperas();      
                        
                    }
                    
                    while (nodoEjecutado.getRafaga() > 0) {
                        
                        tabla[nodoEjecutado.getIndice() - 1][7].setText("Ejecutando");
                        nodoEjecutado.setRafaga(nodoEjecutado.getRafaga() - 1);
                        label3.setText("Proceso en ejecucion: " + nodoEjecutado.getLlave());
                        label4.setText("Tiempo: " + String.valueOf(tiempoGlobal) + " Segundos.");
                        label8.setText("Rafaga restante del proceso: " + nodoEjecutado.getRafaga());
                        dibujarDiagrama(nodoEjecutado.getLlave(), coorX, coorY); 
                        tiempoGlobal++;
                        coorX++;

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Multicolas.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        if (cola2.getTamaño() > 0) {
                                filas++;
                                cola3.insertar(nodoEjecutado.getLlave() + "*", nodoEjecutado.getRafaga(), tiempoGlobal, filas, false);
                                dibujarTabla(nodoEjecutado.getLlave() + "*", nodoEjecutado.getRafaga(), tiempoGlobal);
                                nodoEjecutado.setFinalizacion(tiempoGlobal);
                                llenarRestante();
                                colaEjecutada.eliminar(nodoEjecutado);
                                dibujarTablaEjecucion(cola3, scrollPane10, scrollPane11);
                                llenarBloqueados(colaEjecutada);
                                coorY+=2;
                                colaEjecutada = cola2;
                                
                                // Reiniciar el ciclo para ejecutar el nuevo proceso en cola2
                                break;
                        }else if(cola.getTamaño() > 0){
                                filas++;
                                cola3.insertar(nodoEjecutado.getLlave() + "*", nodoEjecutado.getRafaga(), tiempoGlobal, filas, false);
                                dibujarTabla(nodoEjecutado.getLlave() + "*", nodoEjecutado.getRafaga(), tiempoGlobal);
                                nodoEjecutado.setFinalizacion(tiempoGlobal);
                                llenarRestante();
                                colaEjecutada.eliminar(nodoEjecutado);
                                dibujarTablaEjecucion(cola3, scrollPane10, scrollPane11);
                                llenarBloqueados(colaEjecutada);
                                coorY++;
                                colaEjecutada = cola;
                                
                                // Reiniciar el ciclo para ejecutar el nuevo proceso en cola
                                break;
                        }
                    }
                    if(cola2.getTamaño()==0 && cola.getTamaño() == 0){
                        nodoEjecutado.setFinalizacion(tiempoGlobal);
                        llenarRestante();
                        colaEjecutada.eliminar(colaEjecutada.getCabeza());
                        dibujarTablaEjecucion(cola3, scrollPane10, scrollPane11);
                        llenarBloqueados(colaEjecutada);
                        coorY++;
                    }
                    

                }
                if (cola.getTamaño() == 0 & cola2.getTamaño() ==0) {
                    cola3.desocupar();
                    colaEjecutada.desocupar();
                    colaEjecutada = cola2;
                }
                  
            }
            label3.setText("Proceso en ejecucion: Ninguno");
            scrollPane4.removeAll();
            scrollPane4.repaint();
        } catch (Exception e) {

        }
    }while(true);    
    }
}
