package bo.edu.uagrm.ficct.inf310sb.arboles;

import bo.edu.uagrm.ficct.inf310sb.arboles.Excepciones.ExcepcionClaveNoExiste;
import bo.edu.uagrm.ficct.inf310sb.arboles.Excepciones.ExcepcionOrdenInvalido;

import java.util.Stack;

public class ArbolB <K extends Comparable<K>, V> extends ArbolMViasBusqueda<K,V>{
    private int nroMaximoDeDatos;
    private int nroMinimoDeDatos;
    private int nroMinimoDeHijos;

    public ArbolB() {
        super();
        this.nroMaximoDeDatos = 2;
        this.nroMinimoDeDatos = 1;
        this.nroMinimoDeHijos = 2;

    }

    public ArbolB(int orden) throws ExcepcionOrdenInvalido {
        super(orden);
        this.nroMaximoDeDatos = super.orden - 1;
        this.nroMinimoDeDatos = this.nroMaximoDeDatos / 2;
        this.nroMinimoDeHijos = this.nroMinimoDeDatos + 1;
    }

    @Override
    public void insertar(K claveAInsertar, V valorAInsertar) {
        if (super.esArbolVacio()) {
            super.raiz = new NodoMVias<>(super.orden + 1, claveAInsertar, valorAInsertar);
            return;
        }
        Stack<NodoMVias<K,V>> pilaDeAncestros = new Stack<>();
        NodoMVias<K,V> nodoActual = this.raiz;

        while (!NodoMVias.esNodoVacio(nodoActual)) {
            int posicionClaveExistente = super.existeClaveEnNodo(nodoActual, claveAInsertar);
            if (posicionClaveExistente != POSICION_INVALIDA) {
                nodoActual.setValor(posicionClaveExistente, valorAInsertar);
                nodoActual = NodoMVias.nodoVacio();
            } else {
                if (nodoActual.esHoja()) {
                    super.insertarDatosOrdenadosEnNodo(nodoActual, claveAInsertar, valorAInsertar);
                    if (nodoActual.cantidadDeClavesNoVacias() > this.nroMaximoDeDatos) {
                        this.dividir(nodoActual, pilaDeAncestros);
                    }
                    nodoActual = NodoMVias.nodoVacio();
                } else {
                    int posicionPorDondeBajar = super.posicionPorDondeBajar(nodoActual, claveAInsertar);
                    pilaDeAncestros.push(nodoActual);
                    nodoActual = nodoActual.getHijo(posicionPorDondeBajar);
                }
            }
        }//fin del ciclo

    }

    private void dividir(NodoMVias<K, V> nodoActual, Stack<NodoMVias<K, V>> pilaDeAncestros) {
        int pivote = nroMaximoDeDatos / 2;
        if (pilaDeAncestros.isEmpty()) {
            NodoMVias<K,V> padre = new NodoMVias<>(orden +1, nodoActual.getClave(pivote), nodoActual.getValor(pivote));
            NodoMVias<K,V> hijoDer = new NodoMVias<>(orden + 1);
            int j = 0;
            for (int i = pivote + 1; i < orden; i++) {
                hijoDer.setClave(j, nodoActual.getClave(i));
                hijoDer.setValor(j, nodoActual.getValor(i));
                hijoDer.setHijo(j, nodoActual.getHijo(i));
                nodoActual.setClave(i, null);
                nodoActual.setValor(i, null);
                nodoActual.setHijo(i, null);
                j++;
            }
            hijoDer.setHijo(j, nodoActual.getHijo(orden));
            this.raiz = padre;
            nodoActual.setClave(pivote, null);
            nodoActual.setValor(pivote, null);
            this.raiz.setHijo(0, nodoActual);
            this.raiz.setHijo(raiz.cantidadDeClavesNoVacias(), hijoDer);

        } else {

            NodoMVias<K,V> nodoAncestro = pilaDeAncestros.pop();
            NodoMVias<K,V> hijoDer = new NodoMVias<>(orden + 1);
            //pasamos los valores derechos del pivote  al nuevo nodo
            int j = 0;
            for (int i = pivote + 1; i < orden; i++) {
                hijoDer.setClave(j, nodoActual.getClave(i));
                hijoDer.setValor(j, nodoActual.getValor(i));
                hijoDer.setHijo(j, nodoActual.getHijo(i));
                nodoActual.setClave(i, null);
                nodoActual.setValor(i, null);
                nodoActual.setHijo(i, null);
                j++;
            }
            hijoDer.setHijo(j, nodoActual.getHijo(orden));
            nodoActual.setHijo(orden, null);


            this.insertarDatosOrdenadosEnNodo(nodoAncestro, nodoActual.getClave(pivote), nodoActual.getValor(pivote));
            K clavePivote = nodoActual.getClave(pivote);
            V valorPivote = nodoActual.getValor(pivote);
            int posicionDonSetear = this.posicionPorDondeBajar(nodoAncestro, clavePivote);
            nodoAncestro.setHijo(posicionDonSetear - 1, nodoActual);
            nodoAncestro.setHijo(posicionDonSetear, hijoDer);
            nodoActual.setClave(pivote, null);
            nodoActual.setValor(pivote, null);
            if (nodoAncestro.cantidadDeClavesNoVacias() > nroMaximoDeDatos) {
                dividir(nodoAncestro, pilaDeAncestros);
            }else {
                return;
            }

        }

    }

    @Override
    public V eliminar(K claveAEliminar) {
        if (claveAEliminar == null){
            throw new IllegalArgumentException("clave a eliminar no puede ser nula");
        }

        Stack<NodoMVias<K,V>> pilaDeAncestros = new Stack<>();
        NodoMVias<K,V> nodoActual = this.buscarNodoDeLaClave(claveAEliminar, pilaDeAncestros);

        if (!NodoMVias.esNodoVacio(nodoActual)) {
            throw new ExcepcionClaveNoExiste("la clave no existe en el arbol");
        }

        int posicionDeLaClaveEnElNodo = this.posicionPorDondeBajar(nodoActual, claveAEliminar) - 1;
        V valorARetornar = nodoActual.getValor(posicionDeLaClaveEnElNodo);
        if (nodoActual.esHoja()) {
            super.eliminarDatosDeNodo(nodoActual, posicionDeLaClaveEnElNodo);
            if (nodoActual.cantidadDeClavesNoVacias() < this.nroMinimoDeDatos) {
                if (pilaDeAncestros.isEmpty()) {
                    if (nodoActual.cantidadDeClavesNoVacias() == 0) {
                        super.vaciar();
                    }
                } else {
                    this.prestarseOFusionarse(nodoActual, pilaDeAncestros);
                }
            }
        } else {
            pilaDeAncestros.push(nodoActual);
            NodoMVias<K,V> nodoDelPredecesor = this.buscarNodoDelPredecesor(pilaDeAncestros,
                    nodoActual.getHijo(posicionDeLaClaveEnElNodo));
            int posicionDelPredecesor = nodoDelPredecesor.cantidadDeClavesNoVacias() - 1;
            K clavePredecesora = nodoDelPredecesor.getClave(posicionDelPredecesor);
            V valorPrdecesor = nodoDelPredecesor.getValor(posicionDelPredecesor);
            super.eliminarDatosDeNodo(nodoDelPredecesor, posicionDelPredecesor);
            nodoActual.setClave(posicionDeLaClaveEnElNodo, clavePredecesora);
            nodoActual.setValor(posicionDeLaClaveEnElNodo, valorPrdecesor);
            if (nodoDelPredecesor.cantidadDeClavesNoVacias() < this.nroMaximoDeDatos) {
                this.prestarseOFusionarse(nodoDelPredecesor, pilaDeAncestros);
            }
        }
        return valorARetornar;
    }

    private NodoMVias<K, V> buscarNodoDelPredecesor(Stack<NodoMVias<K, V>> pilaDeAncestros, NodoMVias<K, V> hijo) {

        return null;
    }

    private void prestarseOFusionarse(NodoMVias<K,V> nodoActual, Stack<NodoMVias<K,V>> pilaDeAncestros) {
    }

    private NodoMVias<K,V> buscarNodoDeLaClave(K claveAEliminar, Stack<NodoMVias<K,V>> pilaDeAncestros) {
        return null;
    }


    private void insertarHijoEnNodo (NodoMVias<K, V> nodoActual, NodoMVias<K, V> hijo) {

    }


}
