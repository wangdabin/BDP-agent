package com.sky.agent.montior;

import java.text.DecimalFormat;
import java.util.Properties;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarNotImplementedException;

public class LoadInfoImpl implements LoadInfo {
	/**
	 * 创建sigar对象用来获取系统信息
	 */
	private Sigar sigar = new Sigar();
	private CpuPerc cpuCerc;
	private Properties props = System.getProperties();
	private Mem mem;

	public String getCpuInfo() {
		try {
			/**
			 * 获取cpu信息
			 */
			cpuCerc = sigar.getCpuPerc();
			/**
			 * 保留两位小数
			 */
			DecimalFormat df = new DecimalFormat("#.00");
			/**
			 * 如果cpu使用率为零时，不保留小数位
			 */
			if (cpuCerc.getCombined() == 0) {
				return 0 + "%";
			} else {
				return df.format(cpuCerc.getCombined() * 100) + "%";
			}
		} catch (SigarException e) {
			e.printStackTrace();
		}
		return String.valueOf(cpuCerc.getCombined());
	}

	public String getRamInfo() {
		try {
			/**
			 * 获取系统内存信息
			 */
			mem = sigar.getMem();
		} catch (SigarException e) {
			e.printStackTrace();
		}
		/**
		 * 返回系统内存使用量
		 */
		return mem.getUsed() / 1024 / 1024 + "M";
	}

	public String getNetworkInfo() {
		String ifNames[] = null;
		String txbyte = null;
		try {
			ifNames = sigar.getNetInterfaceList();
		} catch (SigarException e2) {
			e2.printStackTrace();
		}
		String name = ifNames[0];
		try {
			/**
			 * 获取网络流量
			 */
			NetInterfaceStat ifstat = sigar.getNetInterfaceStat(name);
			/**
			 * 获取发送的总字节数
			 */
			txbyte = ifstat.getTxBytes() + "byte";
		} catch (SigarNotImplementedException e) {
		} catch (SigarException e) {
			System.out.println(e.getMessage());
		}
		return txbyte;
	}

	public String getSystemInfo() {
		/**
		 * 获取操作系统名称
		 */
		return props.getProperty("os.name");
	}

	public String getRunLoadInfo() {
		
		/**
		 * 获取运行环境信息
		 */
		String runload = props.getProperty("java.vm.name");
		return runload;
	}

	public String getVmRamInfo() {
		/**
		 * 获取虚拟机剩余内存
		 */
		return Runtime.getRuntime().freeMemory() / (1024 * 1024) + "M";
	}
}