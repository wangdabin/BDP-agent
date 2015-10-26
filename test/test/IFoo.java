package test;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.sun.xml.internal.bind.AnyTypeAdapter;

@XmlJavaTypeAdapter(AnyTypeAdapter.class)
interface IFoo {
	
	String getName();
}