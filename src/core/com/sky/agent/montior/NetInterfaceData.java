package com.sky.agent.montior;
import java.util.ArrayList;    
import java.util.Arrays;
import java.util.List;    
    
import org.hyperic.sigar.NetInterfaceConfig;    
import org.hyperic.sigar.NetInterfaceStat;    
import org.hyperic.sigar.ProcExe;
import org.hyperic.sigar.Sigar;    
import org.hyperic.sigar.SigarException;    
    
import com.thoughtworks.xstream.XStream;    
    
/**  
 * 网卡信息、接口数据、流量  
 *   
 * 使用Sigar获得网卡信息  
 *   
 */    
public class NetInterfaceData {    
    
    private NetInterfaceConfig config;    
    private NetInterfaceStat stat;    
    private long rxbps;
    private long txbps;    
    
    public NetInterfaceData() {}    
    
    public void populate(Sigar sigar, String name)    
        throws SigarException {    
    
        config = sigar.getNetInterfaceConfig(name);    
    
        try {    
            long start = System.currentTimeMillis();    
            NetInterfaceStat statStart = sigar.getNetInterfaceStat(name);    
            long rxBytesStart = statStart.getRxBytes();    
            long txBytesStart = statStart.getTxBytes();    
            Thread.sleep(1000);    
            long end = System.currentTimeMillis();    
            NetInterfaceStat statEnd = sigar.getNetInterfaceStat(name);    
            long rxBytesEnd = statEnd.getRxBytes();    
            long txBytesEnd = statEnd.getTxBytes();    
                
            rxbps = (rxBytesEnd - rxBytesStart)*8/(end-start)*1000;    
            txbps = (txBytesEnd - txBytesStart)*8/(end-start)*1000;    
            stat = sigar.getNetInterfaceStat(name);    
        } catch (SigarException e) {    
                
        } catch (Exception e) {    
                
        }    
    }    
    
    public static NetInterfaceData gather(Sigar sigar, String name)    
        throws SigarException {    
        
        NetInterfaceData data = new NetInterfaceData();    
        data.populate(sigar, name);    
        return data;    
    }    
    
    public NetInterfaceConfig getConfig() {    
        return config;    
    }    
    
    public NetInterfaceStat getStat() {    
        return stat;    
    }    
        
        
        
    public long getRxbps() {    
        return rxbps;    
    }    
    
    public long getTxbps() {    
        return txbps;    
    }    
    
    public static void main(String[] args) throws Exception {    
        Sigar sigar = new Sigar();    
        ProcExe pexe = sigar.getProcExe(1115);
        System.out.println("getName = " + pexe.getName());
        System.out.println("getCwd = " + pexe.getCwd());
        System.out.println("toString = " + pexe.toString());
        System.out.println("toMap = " + pexe.toMap());
        
        System.out.println(Arrays.toString(sigar.getProcArgs(1115)));
        
//        String[] netIfs = sigar.getNetInterfaceList();    
//        List netIfList = new ArrayList();    
//        for ( String name:netIfs ) {    
//            NetInterfaceData netIfData1 = NetInterfaceData.gather(sigar, name);    
//            netIfList.add(netIfData1);    
//        }    
//        XStream xstream = new XStream();    
//        xstream.alias("NetInterfaceDatas", List.class);    
//        xstream.alias("NetInterfaceData", NetInterfaceData.class);    
//        System.out.println(xstream.toXML(netIfList));    
    }    
}    