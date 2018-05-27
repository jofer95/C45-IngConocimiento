/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.c45.maven;

import static com.mycompany.c45.maven.ExcelReader.SAMPLE_XLSX_FILE_PATH;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import modelos.Columna;
import modelos.Nodo;
import modelos.ResultadoPorColumna;
import modelos.SumaTiposPorColumna;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.formula.functions.Column;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 *
 * @author jorgebarraza
 */
public class NegocioC45 {

    public static final String SAMPLE_XLSX_FILE_PATH = "./pruebaPoi.xlsx";
    private ArrayList<Columna> tablaPrincipal;
    private ArrayList<Columna> columnaPrincipal;
    private double entropiaGlobal;
    private int totalColumnas;
    private int totalRegistros;
    private ArrayList<Nodo> arbolFinal = new ArrayList<>();
    private Nodo raiz = new Nodo();
    private Nodo nodoActual = new Nodo();
    private ArrayList<ResultadoPorColumna> listaNodosSiNoPaso2 = new ArrayList<>();

    public static void main(String[] args) throws IOException, InvalidFormatException {
        NegocioC45 context = new NegocioC45();
        context.llenadoDeDatos();
    }

    /**
     * Metodo para llenar las listas y variables necesarias desde el archivo de
     * Excel
     *
     * @throws IOException
     * @throws InvalidFormatException
     */
    public void llenadoDeDatos() throws IOException, InvalidFormatException {
        try {
            Columna columna;
            tablaPrincipal = new ArrayList<>();
            columnaPrincipal = new ArrayList<>();

            //Crear un Workbook desde el archivo Excel (.xls o .xlsx)
            Workbook workbook = WorkbookFactory.create(new File(SAMPLE_XLSX_FILE_PATH));

            //Obteniendo la hoja numero 0
            Sheet sheet = workbook.getSheetAt(0);

            // Create a DataFormatter to format and get each cell's value as String
            DataFormatter dataFormatter = new DataFormatter();

            //Obtener el numero de columnas en el excel
            for (Row row : sheet) { //Por cada Row
                totalColumnas = row.getPhysicalNumberOfCells();
                break;
            }

            //Obtener el total de registros en la tabla
            for (Row row : sheet) { //Por cada Row
                totalRegistros++;
            }
            totalRegistros = totalRegistros - 1;

            for (int i = 0; i < totalColumnas; i++) {
                ArrayList<String> datosColumnas = new ArrayList<>();
                int contador = 0;
                columna = new Columna();
                for (Row row : sheet) { //Por cada Row
                    totalColumnas = row.getPhysicalNumberOfCells();
                    Cell cell = row.getCell(i); //Obtener la celda en la columna a escoger.               
                    if (contador > 0) {
                        datosColumnas.add(dataFormatter.formatCellValue(cell));
                    } else {
                        columna.setNombre(dataFormatter.formatCellValue(cell));
                    }
                    contador++;
                }
                columna.setElemtos(datosColumnas);
                tablaPrincipal.add(columna);
                datosColumnas = null;
                columna = null;
            }

            //Llamar al siguiente paso
            calcularEntropiaGlobar();
        } catch (Exception ex) {
            String error = "Error" + ex.getStackTrace();
        }
    }

    /**
     * Metodo que calcula la entropia globar inicial del paso 1.
     */
    public void calcularEntropiaGlobar() {
        //Obtener el total de los "Si" y los "No"
        ArrayList<String> columnaDeCualesJuegan = tablaPrincipal.get(totalColumnas - 1).getElemtos();
        int totalSi = Collections.frequency(columnaDeCualesJuegan, "Yes");
        int totalNo = Collections.frequency(columnaDeCualesJuegan, "No");
        //Calcular la entropia globar:
        entropiaGlobal = -((double) totalSi / totalRegistros) * Math.log((double) totalSi / totalRegistros) / Math.log(2) - ((double) totalNo / totalRegistros) * Math.log((double) totalNo / totalRegistros) / Math.log(2);
        String x = "";
        paso1();
    }

    public void paso1() {
        int totalSi = 0;
        int totalNo = 0;
        int totalDeUnElementoDeUnaColumna = 0;
        ArrayList<Double> resultadosPorTipoEnColumna = new ArrayList<>();
        ArrayList<SumaTiposPorColumna> resultadosSumaPorcentajesPorColumna = new ArrayList<>();
        double sumaResultadosTipos = 0;
        String nombreColumnaGanadora;
        Nodo nodoPrincipal = new Nodo();
        String elemento = "";
        //Se recorrerán toas las columnas y se le resta 1 porque 1 es por el indice del arreglo.
        for (int i = 0; i < totalColumnas - 1; i++) {
            //Filtrar los elementos de cada columna y hacer un Distinct para contarlos
            List<String> listDistinct = tablaPrincipal.get(i).getElemtos().stream().distinct().collect(Collectors.toList());
            tablaPrincipal.get(i).setNumDeTipos(listDistinct.size());
            //Recorrer cada elemento diferente en la columan para buscar sus "Si" y "No"
            for (int k = 0; k < listDistinct.size(); k++) {
                for (int j = 0; j < tablaPrincipal.get(i).getElemtos().size(); j++) {
                    /*Si el elemnto actual recorriendo de la columna es == al 
                    primero de la lista con el Distinct: validar sus total Si y no */
                    elemento = listDistinct.get(k);
                    if (tablaPrincipal.get(i).getElemtos().get(j).equals(listDistinct.get(k))) {
                        if (tablaPrincipal.get(totalColumnas - 1).getElemtos().get(j).equals("Yes")) {
                            totalSi++;
                        } else {
                            totalNo++;
                        }
                    }
                }
                //If para agregar si un tipo en la columna son puros "Si" o "No"
                //para agregarlos directo
                if (totalSi == 0) {
                    Nodo nodoAgregar = new Nodo();
                    nodoAgregar.setNombre(elemento);
                    Nodo nodoHijo = new Nodo();
                    nodoHijo.setNombre("No");
                    nodoAgregar.getOpcionesNodos().add(nodoHijo);
                    ArrayList<Nodo> arregloNodos = new ArrayList<>();
                    arregloNodos.add(nodoAgregar);
                    nodoPrincipal.setOpcionesNodos(arregloNodos);
                    //Agregar el resultado si sale directo
                    /*ArrayList<String> arregloDeResultadosDirectos = new ArrayList<>();
                    arregloDeResultadosDirectos.add("Yes");
                    nodoPrincipal.setResultados(arregloDeResultadosDirectos);*/
                } else if (totalNo == 0) {
                    Nodo nodoAgregar = new Nodo();
                    nodoAgregar.setNombre(elemento);
                    Nodo nodoHijo = new Nodo();
                    nodoHijo.setNombre("Yes");
                    nodoAgregar.getOpcionesNodos().add(nodoHijo);
                    ArrayList<Nodo> arregloNodos = new ArrayList<>();
                    arregloNodos.add(nodoAgregar);
                    nodoPrincipal.setOpcionesNodos(arregloNodos);
                    //Agregar el resultado si sale directo
                    /*ArrayList<String> arregloDeResultadosDirectos = new ArrayList<>();
                    arregloDeResultadosDirectos.add("No");
                    nodoPrincipal.setResultados(arregloDeResultadosDirectos);*/
                }
                totalDeUnElementoDeUnaColumna = totalSi + totalNo;
                /*Calcular el resultado de ese registro para luego sumarlos y sacar
                    el total de la columna*/
                double subPorcentaje = -((double) totalSi / totalDeUnElementoDeUnaColumna) * Math.log((double) totalSi / totalDeUnElementoDeUnaColumna) / Math.log(2) - ((double) totalNo / totalDeUnElementoDeUnaColumna) * Math.log((double) totalNo / totalDeUnElementoDeUnaColumna) / Math.log(2);
                double totalTipoEntreTotalRegistros = (double) totalDeUnElementoDeUnaColumna / (double) totalRegistros;
                double porcentaje = subPorcentaje * totalTipoEntreTotalRegistros;
                resultadosPorTipoEnColumna.add(porcentaje);
                totalSi = 0;
                totalNo = 0;
            }
            //Sumar todos los porcentajes de los tipos diferentes de la columna
            for (double obj : resultadosPorTipoEnColumna) {
                if (Double.isNaN(obj)) {
                    obj = 0;
                }
                sumaResultadosTipos += obj;
            }
            //AGREGAR A LA LISTA DE DE LAS COLUMNAS SUS SUMAS PARA OBTENER LA GANADORA.
            resultadosPorTipoEnColumna = new ArrayList<>();
            SumaTiposPorColumna totalSuma = new SumaTiposPorColumna();
            totalSuma.setNombreColumna(tablaPrincipal.get(i).getNombre());
            totalSuma.setTotalSumaTipos(sumaResultadosTipos);
            resultadosSumaPorcentajesPorColumna.add(totalSuma);
            sumaResultadosTipos = 0;
        }
        SumaTiposPorColumna ganador = Collections.min(resultadosSumaPorcentajesPorColumna);
        nodoPrincipal.setNombre(ganador.getNombreColumna());
        arbolFinal.add(nodoPrincipal);
        raiz = nodoPrincipal;
        nodoActual = nodoPrincipal;
        String x = "";
        paso2();
    }

    public void paso2() {

        //Recorrer las columnas para obtener la columna del nombre ganador
        int totalSi = 0;
        int totalNo = 0;
        int totalDeUnElementoDeUnaColumna = 0;
        ArrayList<Double> resultadosPorTipoEnColumna = new ArrayList<>();
        ArrayList<SumaTiposPorColumna> resultadosSumaPorcentajesPorColumna = new ArrayList<>();
        double sumaResultadosTipos = 0;
        Nodo nodoPrincipal = new Nodo();
        String elemento = "";
        int numeroDeElementosEnColumna = 0;
        boolean esColumnaGanadora = false;
        String elementoPasoActual = "";
        int indiceColumnaGanadora = 0;
        int contadorParaSalir = 0;
        boolean columnaAgregada = false;
        //Se recorrerán toas las columnas y se le resta 1 porque 1 es por el indice del arreglo.
        //RECORRIENDO LAS COLUMNAS:
        for (int i = 0; i < totalColumnas - 1; i++) {
            indiceColumnaGanadora = obtenerIndiceColumnaGanadora(raiz.getNombre());
            //Si la columna actual es igual a la ganadora general, saltar a la siguiente columna.
            if (tablaPrincipal.get(i).getNombre().equals(raiz.getNombre())) {
                esColumnaGanadora = true;
                indiceColumnaGanadora = i;
            } else {
                esColumnaGanadora = false;
            }
            //Filtrar los elementos de cada columna y hacer un Distinct para contarlos
            List<String> listDistinct = tablaPrincipal.get(i).getElemtos().stream().distinct().collect(Collectors.toList());
            //Recorrer cada elemento diferente en la columan para buscar sus "Si" y "No"
            //RECORRIENDO LA LIASTA DE CADA ELEMENTO (DISTINCT) DE LA COLUMNA i:
            for (int k = 0; k < listDistinct.size(); k++) {
                //RECORRIENDO TODOS LOS ELEMENTOS DE LA COLUMNA i:
                for (int j = 0; j < tablaPrincipal.get(i).getElemtos().size(); j++) {
                    elemento = listDistinct.get(k);
                    //Si el elemento actual de la columna ya esta en el arbol: Saltar al siguiente.
                    if (raiz.getResultados().contains(elemento) && esColumnaGanadora) {
                        columnaAgregada = true;
                        continue;
                    } else if (esColumnaGanadora && elementoPasoActual.equals("")) {
                        elementoPasoActual = elemento;
                        numeroDeElementosEnColumna = Collections.frequency(tablaPrincipal.get(i).getElemtos(), elemento);
                    }

                    /*Si el elemnto actual recorriendo de la columna es == al 
                    primero de la lista con el Distinct: validar sus total Si y no */
                    if (tablaPrincipal.get(i).getElemtos().get(j).equals(listDistinct.get(k))) {
                        if (tablaPrincipal.get(totalColumnas - 1).getElemtos().get(j).equals("Yes")) {
                            if (tablaPrincipal.get(indiceColumnaGanadora).getElemtos().get(j).equals(elementoPasoActual)) {
                                totalSi++;
                            }
                        } else {
                            if (tablaPrincipal.get(indiceColumnaGanadora).getElemtos().get(j).equals(elementoPasoActual)) {
                                totalNo++;
                            }
                        }
                    }
                }

                //VALIDACION DE SALIDA DE RECURSIVIDAD:
                //PARA SALIRSE CUANDO SE TIENEN TODOS LOR RESULTADOS POSIBLES.
                if (contadorParaSalir == listDistinct.size()) {
                    return;
                }

                //If para agregar si un tipo en la columna son puros "Si" o "No"
                //para agregarlos directo
                if (totalSi == 0) {
                    ResultadoPorColumna resultadoPorColumna = new ResultadoPorColumna();
                    resultadoPorColumna.setResultado("No");
                    resultadoPorColumna.setElemento(elemento);
                    resultadoPorColumna.setColumna(tablaPrincipal.get(i).getNombre());
                    listaNodosSiNoPaso2.add(resultadoPorColumna);
                    //ArrayList<Nodo> arregloNodos = new ArrayList<>();
                    //arregloNodos.add(nodoAgregar);
                    //nodoPrincipal.getOpcionesNodos().add(nodoAgregar);
                    //Agregar el resultado si sale directo
                    ArrayList<String> arregloDeResultadosDirectos = new ArrayList<>();
                    arregloDeResultadosDirectos.add(elemento);
                    nodoPrincipal.setResultados(arregloDeResultadosDirectos);
                } else if (totalNo == 0) {
                    ResultadoPorColumna resultadoPorColumna = new ResultadoPorColumna();
                    resultadoPorColumna.setResultado("Yes");
                    resultadoPorColumna.setElemento(elemento);
                    resultadoPorColumna.setColumna(tablaPrincipal.get(i).getNombre());
                    listaNodosSiNoPaso2.add(resultadoPorColumna);
                    //ArrayList<Nodo> arregloNodos = new ArrayList<>();
                    //arregloNodos.add(nodoAgregar);
                    //nodoPrincipal.getOpcionesNodos().add(nodoAgregar);
                    //Agregar el resultado si sale directo
                    ArrayList<String> arregloDeResultadosDirectos = new ArrayList<>();
                    arregloDeResultadosDirectos.add(elemento);
                    nodoPrincipal.setResultados(arregloDeResultadosDirectos);
                }
                totalDeUnElementoDeUnaColumna = totalSi + totalNo;
                /*Calcular el resultado de ese registro para luego sumarlos y sacar
                    el total de la columna*/
                double subPorcentaje = -((double) totalSi / totalDeUnElementoDeUnaColumna) * Math.log((double) totalSi / totalDeUnElementoDeUnaColumna) / Math.log(2) - ((double) totalNo / totalDeUnElementoDeUnaColumna) * Math.log((double) totalNo / totalDeUnElementoDeUnaColumna) / Math.log(2);
                double totalTipoEntreTotalRegistros = (double) totalDeUnElementoDeUnaColumna / (double) numeroDeElementosEnColumna;
                double porcentaje = subPorcentaje * totalTipoEntreTotalRegistros;
                resultadosPorTipoEnColumna.add(porcentaje);
                totalSi = 0;
                totalNo = 0;
            }
            //Sumar todos los porcentajes de los tipos diferentes de la columna
            //RECORRIENDO TODOS LOS RESULTADOS DE CADA ELEMENTO DE LA COLUMNA i:
            for (double obj : resultadosPorTipoEnColumna) {
                if (Double.isNaN(obj)) {
                    obj = 0;
                }
                sumaResultadosTipos += obj;
            }

            //Si es la columna ganadora, no agregar los resultados de esa columna.
            if (!esColumnaGanadora) {
                //AGREGAR A LA LISTA DE DE LAS COLUMNAS SUS SUMAS PARA OBTENER LA GANADORA.
                SumaTiposPorColumna totalSuma = new SumaTiposPorColumna();
                totalSuma.setNombreColumna(tablaPrincipal.get(i).getNombre());
                totalSuma.setTotalSumaTipos(sumaResultadosTipos);
                //Filtrar los elementos de cada columna y hacer un Distinct para contarlos
                //listDistinct = tablaPrincipal.get(obtenerIndiceColumnaGanadora(ganador.getNombreColumna())).getElemtos().stream().distinct().collect(Collectors.toList());
                ArrayList<String> arrayElementosDist = new ArrayList<>(listDistinct);
                totalSuma.setTiposPorColumna(arrayElementosDist);
                resultadosSumaPorcentajesPorColumna.add(totalSuma);
                sumaResultadosTipos = 0;
            }
            resultadosPorTipoEnColumna = new ArrayList<>();
        }
        SumaTiposPorColumna ganador = Collections.min(resultadosSumaPorcentajesPorColumna);
        nodoPrincipal.setNombre(ganador.getNombreColumna());
        nodoPrincipal.setResultados(ganador.getTiposPorColumna());
        nodoPrincipal.setOpcionesNodos(obtenerNodosResultadoPorColumna(nodoPrincipal));
        raiz.getOpcionesNodos().add(nodoPrincipal);
        raiz.getResultados().add(elementoPasoActual);
        String x = "";
        paso2();
    }

    /**
     * Metodo para obtener la cantidad de elementos de la columna ganadora en su
     * columna para obtener la multiplicación del paso 2 de cada tipo y obtener
     * su resultado: Ejemplo 5 Sunny en la columna Outlook
     *
     * @param elemento elemento a buscar
     * @return
     */
    public int cantidadTiposActual(String elemento) {
        int numeroDeElementosEnColumna = 0;
        String elementoActual;
        //Se recorrerán toas las columnas y se le resta 1 porque 1 es por el indice del arreglo.
        for (int i = 0; i < totalColumnas - 1; i++) {
            //Filtrar los elementos de cada columna y hacer un Distinct para contarlos
            List<String> listDistinct = tablaPrincipal.get(i).getElemtos().stream().distinct().collect(Collectors.toList());
            //Recorrer cada elemento diferente en la columan para buscar sus "Si" y "No"
            for (int k = 0; k < listDistinct.size(); k++) {
                elementoActual = listDistinct.get(k);
                for (int j = 0; j < tablaPrincipal.get(i).getElemtos().size(); j++) {
                    if (elementoActual.equals(elemento)) {
                        return numeroDeElementosEnColumna = Collections.frequency(tablaPrincipal.get(i).getElemtos(), elemento);
                    }
                }
            }
        }
        //Si no se encontro el resultado, regresa 0
        return 0;
    }

    /**
     * Metodo para obtener el indice de la columna que gano para utilizar en el
     * proceso de obtener los si y no donde coincida el elemento
     *
     * @param columna columna a buscar
     * @return
     */
    public int obtenerIndiceColumnaGanadora(String columna) {
        for (int i = 0; i < totalColumnas - 1; i++) {
            if (tablaPrincipal.get(i).getNombre().equals(raiz.getNombre())) {
                return i;
            }
        }
        return 0;
    }

    /**
     * Metodo para obtener los resultados "Si" y "No" de la columna ganadora
     * @param nodoGanador nodo ganador para obtener sus hijos resultados.
     * @return Regresa una lista de los resultados
     */
    public ArrayList<Nodo> obtenerNodosResultadoPorColumna(Nodo nodoGanador) {
        ArrayList<Nodo> resultados = new ArrayList<>();
        for (ResultadoPorColumna obj : listaNodosSiNoPaso2) {
            if (obj.getColumna().equals(nodoGanador.getNombre())) {
                if (nodoGanador.getResultados().contains(obj.getElemento())) {
                    Nodo nodoResultado = new Nodo();
                    nodoResultado.setNombre(obj.getResultado());
                    resultados.add(nodoResultado);
                }
            }
        }
        return resultados;
    }

}
