/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

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

    public double getTotalSumaTipos() {
        return totalSumaTipos;
    }

    public void setTotalSumaTipos(double totalSumaTipos) {
        this.totalSumaTipos = totalSumaTipos;
    }
    
    String nombreColumna;
    Double totalSumaTipos;

    @Override
    public int compareTo(SumaTiposPorColumna o) {
        return this.totalSumaTipos.compareTo(o.getTotalSumaTipos());
    }
    
}
