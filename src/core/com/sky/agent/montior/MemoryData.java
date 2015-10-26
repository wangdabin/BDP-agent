package com.sky.agent.montior;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Swap;

import com.thoughtworks.xstream.XStream;
    
    
/**  
 * 内存数据  
 *   
 * 使用Sigar获得系统内存信息  
 *   
 */    
public class MemoryData {    
    private Mem mem;    
    private Swap swap;    
    
    public MemoryData() {    
    }    
    
    public void populate(Sigar sigar) throws SigarException {    
        mem = sigar.getMem();    
        swap = sigar.getSwap();    
    }    
    
    public static MemoryData gather(Sigar sigar) throws SigarException {    
        MemoryData data = new MemoryData();    
        data.populate(sigar);    
        return data;    
    }    
        
    public static void main(String[] args) throws Exception {    
        Sigar sigar = new Sigar();    
        MemoryData memData = MemoryData.gather(sigar);    
        XStream xstream = new XStream();    
        xstream.alias("MemData", MemoryData.class);    
        System.out.println(xstream.toXML(memData));    
    }    
    
}   