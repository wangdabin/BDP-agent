package com.sky.agent.montior;

import org.hyperic.sigar.Cpu;
import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

import com.thoughtworks.xstream.XStream;

public class CpuData {    
    private CpuInfo info;    
    private CpuPerc perc;    
    private Cpu timer;    
    
    public CpuData() {    
    }    
    
    public void populate(Sigar sigar) throws SigarException {    
        info = sigar.getCpuInfoList()[0];    
        perc = sigar.getCpuPerc();    
        timer = sigar.getCpu();    
    }    
    
    public static CpuData gather(Sigar sigar) throws SigarException {    
        CpuData data = new CpuData();    
        data.populate(sigar);    
        return data;    
    }    
    
    public static void main(String[] args) throws Exception {    
        Sigar sigar = new Sigar();    
        CpuData cpuData = CpuData.gather(sigar);    
        XStream xstream = new XStream();
        xstream.alias("CpuData", CpuData.class);
        System.out.println(xstream.toXML(cpuData));    
    }    
    
} 