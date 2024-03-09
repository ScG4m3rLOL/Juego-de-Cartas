package juegocartas;

import java.util.Arrays;
import java.util.Random;
import javax.swing.JPanel;

public class Jugador {

    public int TOTAL_CARTAS = 10;
    private Random r = new Random();
    private Carta[] cartas = new Carta[TOTAL_CARTAS];
    private Carta[] cartasPica, cartasTrebol, cartasCorazon, cartasDiamante;
    private int cantPica, cantTrebol, cantCorazon, cantDiamante;
    private int[] enPica, enTrebol, enCorazon, enDiamante;
//    

    public void repartir() {
        for (int i = 0; i < cartas.length; i++) {
            cartas[i] = new Carta(r);
        }
    }

    public void mostrar(JPanel pnl) {
        pnl.removeAll();
        for (int i = 0; i < cartas.length; i++) {
            cartas[i].mostrar(pnl, 5 + i * 40, 5);
        }
        pnl.repaint();
    }
    
    public String getPuntaje() {

         enPica = numerosConsecutivos(cartasConsecutivas(cartasPica));
         enTrebol = numerosConsecutivos(cartasConsecutivas(cartasTrebol));
         enCorazon = numerosConsecutivos(cartasConsecutivas(cartasCorazon));
         enDiamante = numerosConsecutivos(cartasConsecutivas(cartasDiamante));

        marcaNumConsecutivos(enPica, cartas, Pinta.PICA);
        marcaNumConsecutivos(enTrebol, cartas, Pinta.TREBOL);
        marcaNumConsecutivos(enCorazon, cartas, Pinta.CORAZON);
        marcaNumConsecutivos(enDiamante, cartas, Pinta.DIAMANTE);

        return calcularElPuntaje(cartas);
    }

    public String getGrupos() {
        // Contar cartas por pinta
        cantPica = contPorPinta(cartas, Pinta.PICA);
        cantTrebol = contPorPinta(cartas, Pinta.TREBOL);
        cantCorazon = contPorPinta(cartas, Pinta.CORAZON);
        cantDiamante = contPorPinta(cartas, Pinta.DIAMANTE);

        // Ordenar cartas por pinta
        cartasPica = ordPorPinta(cartas, Pinta.PICA, cantPica);
        cartasTrebol = ordPorPinta(cartas, Pinta.TREBOL, cantTrebol);
        cartasCorazon = ordPorPinta(cartas, Pinta.CORAZON, cantCorazon);
        cartasDiamante = ordPorPinta(cartas, Pinta.DIAMANTE, cantDiamante);

        // Contar cartas por nombre
        int[] contadores = new int[NombreCarta.values().length];
        for (Carta carta : cartas) {
            contadores[carta.getNombre().ordinal()]++;
        }

        // Marcar cartas que pertenecen a un grupo
        for (Carta carta : cartas) {
            int ordinal = carta.getNombre().ordinal();
            if (contadores[ordinal] >= 2) {
                carta.setFormaGrupo(true);
            }
        }

        // Mostrar mensajes de escaleras
        String[] mensajesEscaleras = {
            mostrarConsecutivas(cartasConsecutivas(cartasPica), Pinta.PICA),
            mostrarConsecutivas(cartasConsecutivas(cartasTrebol), Pinta.TREBOL),
            mostrarConsecutivas(cartasConsecutivas(cartasCorazon), Pinta.CORAZON),
            mostrarConsecutivas(cartasConsecutivas(cartasDiamante), Pinta.DIAMANTE),
        };

        // Obtener mensaje total
        return mensTotal(mensajesEscaleras, contarGrupos(contadores), contadores);
    }
    
    private int contarGrupos(int[] contadores) {
        int Grupos = 0;
        for (int contador : contadores) {
            if (contador >= 2) {
                Grupos++;
            }
        }
        return Grupos;
    }

    public static String mensTotal(String[] mensajesEscaleras, int totalGrupos, int[] contadores) {
    StringBuilder mensaje = new StringBuilder();

        if (totalGrupos > 0) {
            mensaje.append("Grupos encontrados:\n");
            for (int i = 0; i < contadores.length; i++) {
                if (contadores[i] >= 2) {
                    mensaje.append(Grupo.values()[contadores[i]])
                            .append(" de ")
                            .append(NombreCarta.values()[i])
                            .append("\n");
                }
            }
        }

        StringBuilder mensajeCompleto = new StringBuilder();
        for (String mensajeEscalera : mensajesEscaleras) {
            mensajeCompleto.append(mensajeEscalera);
        }

        if (mensajeCompleto.length() > 0) {
            mensaje.append("Escaleras encontradas:\n")
                    .append(mensajeCompleto)
                    .append("\n");
        }

        if (mensaje.length() == 0) {
            mensaje.append("No hay grupos");
        }

        return mensaje.toString();
    }
    
    public void ordBurbuja() {
    int n = cartas.length;

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (cartas[j].getIndice() > cartas[j + 1].getIndice()) {
                    Carta temp = cartas[j];
                    cartas[j] = cartas[j + 1];
                    cartas[j + 1] = temp;
                }
            }
        }
    }

    public Carta[] ordSelec(Carta cartas[]) {
        int n = cartas.length;

        for (int i = 0; i < n - 1; i++) {
            int indiceMinimo = encontrarIndiceMinimo(cartas, i, n);

            intercambiarElementos(cartas, i, indiceMinimo);
        }

        return cartas;
}

    private int encontrarIndiceMinimo(Carta cartas[], int inicio, int fin) {
        int indiceMinimo = inicio;

        for (int j = inicio + 1; j < fin; j++) {
            if (cartas[j].getIndice() < cartas[indiceMinimo].getIndice()) {
                indiceMinimo = j;
            }
        }

        return indiceMinimo;
}

    private void intercambiarElementos(Carta cartas[], int i, int j) {
        Carta temp = cartas[j];
        cartas[j] = cartas[i];
        cartas[i] = temp;
    }
    
    public int contPorPinta(Carta[] cartas, Pinta pinta) {
        int contador = 0;

        for (Carta carta : cartas) {
            if (carta.getPinta() == pinta) {
                contador++;
            }
        }

        return contador;
    }

    public Carta[] ordPorPinta(Carta cartas[], Pinta pinta, int numeroCartas) {
        int indice = 0;
        Carta[] Pinta = new Carta[numeroCartas];

        for (Carta carta : cartas) {
            if (carta.getPinta() == pinta) {
                Pinta[indice] = carta;
                indice++;
                if (indice >= numeroCartas) {
                    break;
                }
            }
        }

        return ordSelec(Pinta);
    }   
    
    public int[][] cartasConsecutivas(Carta cartas[]) {
        if (cartas.length == 0) {
            return new int[0][2];
        }

        int[][] consecutivos = new int[cartas.length][2];
        int grupos = 0;

        int iniGrupo = cartas[0].getNombre().ordinal();
        int finGrupo = iniGrupo;

        for (int i = 1; i < cartas.length; i++) {
            if (cartas[i].getNombre().ordinal() == finGrupo + 1) {
                finGrupo = cartas[i].getNombre().ordinal();
            } else {
                consecutivos[grupos][0] = iniGrupo;
                consecutivos[grupos][1] = finGrupo;
                grupos++;

                iniGrupo = finGrupo = cartas[i].getNombre().ordinal();
            }
        }

        consecutivos[grupos][0] = iniGrupo;
        consecutivos[grupos][1] = finGrupo;
        grupos++;

        int[][] resultado = new int[grupos][2];
        for (int i = 0; i < grupos; i++) {
            resultado[i][0] = consecutivos[i][0];
            resultado[i][1] = consecutivos[i][1];
        }

        return resultado;
    }

    public static String mostrarConsecutivas(int[][] grupos, Pinta pinta) {
        StringBuilder resultado = new StringBuilder();
        NombreCarta[] nombres = NombreCarta.values();

        for (int i = 0; i < grupos.length; i++) {
            int ini = grupos[i][0] + 1;
            int fin = grupos[i][1] + 1;

            if (ini != fin) {
                resultado.append("Escalera de ").append(pinta).append(": ");

                for (int num = ini; num <= fin; num++) {
                    resultado.append(nombres[num - 1]);
                    if (num < fin) {
                        resultado.append(", ");
                    }
                }

                resultado.append("\n");
            }
        }

        return resultado.toString();
    }
    
    public static int[] numerosConsecutivos(int[][] grupos) {
        int capacidad = grupos.length * 10;
        int[] numerosConsecutivos = new int[capacidad];
        int resulIndex = 0;

        for (int[] grupo : grupos) {
            int ini = grupo[0] + 1;
            int fin = grupo[1] + 1;

            if (ini != fin) {
                for (int num = ini; num <= fin; num++) {
                    if (resulIndex == numerosConsecutivos.length) {
                        int nuevaCapacidad = numerosConsecutivos.length * 2;
                        int[] nuevoArreglo = new int[nuevaCapacidad];
                        System.arraycopy(numerosConsecutivos, 0, nuevoArreglo, 0, resulIndex);
                        numerosConsecutivos = nuevoArreglo;
                    }
                    numerosConsecutivos[resulIndex++] = num;
                }
            }
        }

        // Redimensionar el arreglo al tamaño real
        int[] resultado = new int[resulIndex];
        System.arraycopy(numerosConsecutivos, 0, resultado, 0, resulIndex);

        return resultado;
    }

    public static void marcaNumConsecutivos(int[] numerosConsecutivos, Carta[] cartas, Pinta pinta) {

        for (int i = 0; i < numerosConsecutivos.length; i++) {
            int num = numerosConsecutivos[i];

            for (Carta carta : cartas) {
                if (carta.getNombre().ordinal() == (num - 1) && carta.getPinta() == pinta) {
                    carta.setFormaGrupo(true);
                    break;
                }
            }
        }
    }

    
    public String calcularElPuntaje(Carta[] cartas) {
        int suma = 0;
        StringBuilder cartasNoFormanGrupo = new StringBuilder();

        for (Carta carta : cartas) {
            if (!carta.estaEnGrupo()) {
                int ordinal = carta.getNombre().ordinal();
                suma += (ordinal == 0 || (ordinal >= 10 && ordinal <= 12)) ? 10 : (ordinal + 1);

                // Agrega el nombre de la carta y su pinta al mensaje de cartas que no forman grupo.
                if (cartasNoFormanGrupo.length() > 0) {
                    cartasNoFormanGrupo.append("\n");
                }
                cartasNoFormanGrupo.append(carta.getNombre()).append(" de ").append(carta.getPinta());
            }
        }

        // Verifica si todas las cartas forman grupo o no.
        if (cartasNoFormanGrupo.length() == 0) {
            return "Todas las cartas forman grupo, en este caso su puntaje es CERO";
        } else {
            return "Su puntaje es: " + suma + "\nLas cartas que no forman grupo son:\n" + cartasNoFormanGrupo.toString();
        }
    }
}
