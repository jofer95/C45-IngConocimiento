/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

import java.util.ArrayList;

/**
 *
 * @author jorgebarraza
 */
public class SumaTiposPorColumna implements Comparable<SumaTiposPorColumna> {

    public String getNombreColumna() {
        return nombreColumna;
    }

    public void setNombreColumna(String nombreColumna) {
        this.nombreColumna = nombreColumna;
    }

    public Double getTotalSumaTipos() {
        return totalSumaTipos;
    }

    public void setTotalSumaTipos(Double totalSumaTipos) {
        this.totalSumaTipos = totalSumaTipos;
    }

    public ArrayList<String> getTiposPorColumna() {
        return tiposPorColumna;
    }

    public void setTiposPorColumna(ArrayList<String> tiposPorColumna) {
        this.tiposPorColumna = tiposPorColumna;
    }
    private String nombreColumna;
    private Double totalSumaTipos;
    private ArrayList<String> tiposPorColumna;

    @Override
    public int compareTo(SumaTiposPorColumna o) {
        return this.totalSumaTipos.compareTo(o.getTotalSumaTipos());
    }
    
}
