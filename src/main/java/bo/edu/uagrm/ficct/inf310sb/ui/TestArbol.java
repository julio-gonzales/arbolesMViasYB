package bo.edu.uagrm.ficct.inf310sb.ui;

import bo.edu.uagrm.ficct.inf310sb.arboles.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class TestArbol {
    public static  void  main(String [] argumentos){

        IArbolBusqueda<Integer,String> arbolDePrueba = new ArbolBinarioBusqueda<>();


        int opcion = 0;
        int subOpcion = 0;
        int nivel = 0;
        Integer clave = 0;
        String valor = "";
        Scanner teclado = new Scanner(System.in);

        System.out.println("ARBOLES DE BUSQUEDA");
        System.out.println("1.  ARBOL BINARIO DE BUSQUEDA");
        System.out.println("2.  AVL");
        System.out.println("Seleccione con que arbol desea trabajar");
        opcion = teclado.nextInt();

        switch (opcion) {
            case 1:
                arbolDePrueba = new ArbolBinarioBusqueda<>();
                break;

            case 2:
                arbolDePrueba = new AVL<>();
                break;
        }


        do {
            System.out.println("MENU");
            System.out.println("1.  INSERTAR");
            System.out.println("2.  BUSCAR");
            System.out.println("3.  ELIMINAR");
            System.out.println("4.  RECORRIDO IN ORDEN");
            System.out.println("5.  RECORRIDO PRE ORDEN");
            System.out.println("6.  RECORRIDO POST ORDEN");
            System.out.println("7.  RECORRIDO POR NIVELES");
            System.out.println("8.  NIVEL");
            System.out.println("9.  ALTURA");
            System.out.println("10.  VACIAR");
            System.out.println("11.  CANTIDAD DE NODOS QUE TIENEN SOLO HIJO IZQUIERDO EN EL ARBOL  (Recursivo)");
            System.out.println("12.  CANTIDAD DE NODOS QUE TIENEN SOLO HIJO IZQUIERDO EN EL ARBOL  (Iterativo)");
            System.out.println("13.  CANTIDAD DE NODOS CON SOLO HIJO IZQUIERDO EN EL NIVEL N");
            System.out.println("14. CANTIDAD DE NODOS CON SOLO HIJO IZQUIERDO DESPUES DEL NIVEL N");
            System.out.println("15.  CANTIDAD DE NODOS CON SOLO HIJO IZQUIERDO ANTES  DEL NIVEL N");
            System.out.println("16. CANTIDAD DE NODOS QUE TIENE AMBOS HIJOS DESDE EL NIVEL N");

            System.out.println("17. SALIR");
            opcion = teclado.nextInt();

            switch (opcion) {
                case 1:
                    System.out.println("DIGITE LA CLAVE: ");
                    clave = teclado.nextInt();
                    System.out.println("DIGITE EL VALOR: ");
                    valor = teclado.next();
                    arbolDePrueba.insertar(clave, valor);
                    break;

                case 2:
                    System.out.println("DIGITE LA CLAVE A BUSCAR");
                    clave = teclado.nextInt();
                    System.out.println("el valor de la clave es: " + arbolDePrueba.buscar(clave));
                    break;

                case 3:
                    System.out.println("DIGITE LA CLAVE A ELIMINAR: ");
                    clave = teclado.nextInt();
                    valor = arbolDePrueba.eliminar(clave);
                    System.out.println("el valor de la clave eliminada es: " + valor);
                    break;
                case 4:
                    System.out.println("RECORRIDO INORDEN: " + arbolDePrueba.recorridoEnInOrden());
                    break;
                case 5:
                    System.out.println("RECORRIDO PRE-ORDEN: " + arbolDePrueba.recorridoEnPreOrden());
                    break;
                case 6:
                    System.out.println("RECORRIDO POST-ORDEN: " + arbolDePrueba.recorridoEnPostOrden());
                    break;
                case 7:
                    System.out.println("RECORRIDO POR NIVELES: " + arbolDePrueba.recorridoPorNiveles());
                    break;
                case 8:
                    System.out.println("EL NIVEL DEL ARBOL ES: " + arbolDePrueba.nivel());
                    break;
                case 9:
                    System.out.println("LA ALTURA DEL ARBOL ES: " + arbolDePrueba.altura());
                    break;
                case 10:
                    arbolDePrueba.vaciar();
                    System.out.println("EL ARBOL SE VACIO");
                    break;

                case 11:
                    System.out.println("la cantidad de nodos con solo hijo izquierdo en el arbol es: " +
                            ((ArbolBinarioBusqueda)arbolDePrueba).cantidadDeNodosConHijoIzquierdo());
                    break;

                case 12:
                    System.out.println("la cantidad de nodos con solo hijo izquierdo en el arbol es: " +
                            ((ArbolBinarioBusqueda)arbolDePrueba).cantidadDeNodosConSoloHijoIzq());
                    break;


                case 13:
                    System.out.println("digite el nivel en que desea buscar");
                    nivel = teclado.nextInt();
                    System.out.println("la cantidad de nodos con solo hijo izquierdo en el " + nivel + " es " +
                            ((ArbolBinarioBusqueda)arbolDePrueba).cantidadDeHijosIzquierdoEnNivel(nivel));
                    break;

                case 14:
                    System.out.println("digite el nivel en que desea buscar");
                    nivel = teclado.nextInt();
                    System.out.println("la cantidad de nodos con solo hijo izquierdo despues del  " + nivel + " es " +
                            ((ArbolBinarioBusqueda)arbolDePrueba).cantidadDeHijosIzqDespuesDelNivel(nivel));
                    break;

                case 15:
                    System.out.println("digite el nivel en que desea buscar");
                    nivel = teclado.nextInt();
                    System.out.println("la cantidad de nodos con solo hijo izquierdo antes  del " + nivel + " es " +
                            ((ArbolBinarioBusqueda)arbolDePrueba). cantidadDeHijosIzqAntesDelNivel(nivel));
                    break;

                case 16:

                    System.out.println("digite el nivel");
                    nivel = teclado.nextInt();
                    System.out.println("la cantidad de nodos con dos hijos desde el nivel "+ nivel + " es " +
                            ((ArbolBinarioBusqueda)arbolDePrueba).cantidadDeNodosConAmbosHijos(nivel));
                    break;

            }

        }

        while(opcion != 17);


    }



}
