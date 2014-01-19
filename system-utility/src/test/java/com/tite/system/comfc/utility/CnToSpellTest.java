package com.tite.system.comfc.utility;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.tite.system.comfc.utility.CnToSpell;

public class CnToSpellTest {

	@Test
	public void testGetFullSpell() {
		String strCN = " 22 < s重 k庆32$#imi市m>&%61， ";
		String strPY = CnToSpell.getFullSpell(strCN);
		assertEquals("er4,er4,zhong4,qing4,san1,er4,shi4,liu4,yi1", strPY);
	}
	
	@Test
	public void testGetFullSpell2() {
		String strCN = " 22 < s重 k庆32$#imi市m>&%61， ";
		String strPY = CnToSpell.getFullSpell2(strCN);
		assertEquals("er,er,s,zhong,k,qing,san,er,shi,m,liu,yi", strPY);
	}
	
	@Test
	public void testGetFullSpell3(){
		String strCN = "俊峰·香格里拉";
		String strPY = CnToSpell.getFullSpell2(strCN);
		assertEquals("jun,feng,xiang,ge,li,la", strPY);
		
	}
	
	@Test
	public void testGetFullSpell4(){
		String strCN = "重庆西永微电子产业园区开发有限公司研发楼二期、三期";
		String strPY = CnToSpell.getFullSpell2(strCN);
		assertEquals("zhong,qing,xi,yong,wei,dian,zi,chan,ye,yuan,qu,kai,fa,you,xian,gong,si,yan,fa,lou,er,qi,san,qi", strPY);
	}
	
	@Test
	public void testGetFullSpell5(){
		String strCN = "金科、大学城项目（1号地块）";
		String strPY = CnToSpell.getFullSpell2(strCN);
		assertEquals("jin,ke,da,xue,cheng,xiang,mu,yi,hao,di,kuai", strPY);
	}
	
	@Test
	public void testGetFullSpell6(){
		String strCN = "石板镇青龙村“农转非“安置房";
		String strPY = CnToSpell.getFullSpell2(strCN);
		assertEquals("shi,ban,zhen,qing,long,cun,nong,zhuan,fei,an,zhi,fang", strPY);
	}
	
	@Test
	public void testGetFullSpell7(){
		String strCN = "“江南华都”";
		String strPY = CnToSpell.getFullSpell2(strCN);
		assertEquals("jiang,nan,hua,dou", strPY);
	}
	
	@Test
	public void testGetFullSpell8() {
		//字符串中不存在汉字时, 就不存在拼音
		String strCN = " shk， ";
		String strPY = CnToSpell.getFullSpell(strCN);
		assertEquals("", strPY);
	}
	
	@Test
	public void testGetFullSpell9() {
		//字符串中不存在汉字时, 就不存在拼音
		String strCN = " shk， ";
		String strPY = CnToSpell.getFullSpell2(strCN);
		assertEquals("", strPY);
	}
	
	@Test
	public void testGetFullSpell10(){
		//"垊"字无法转换为拼音,因此需要预留空缺
		String strCN = "西陇狗垊";
		String strPY = CnToSpell.getFullSpell2(strCN);
		assertEquals("xi,long,gou,null", strPY);
	}
	

	
	@Test
	public void testGetFullSpell11(){
		String strCN = "西湖A区";
		String strPY = CnToSpell.getFullSpell2(strCN);
		assertEquals("xi,hu,a,qu", strPY);
	}
	
	@Test
	public void testToDBC(){
		String strCN = "（江南华都）";
		String strPY = CnToSpell.toDBC(strCN);
		assertEquals("(江南华都)", strPY);
	}
	
	@Test
	public void testToDBC2(){
		String strCN = "江南华都Ａ区";
		String strPY = CnToSpell.toDBC(strCN);
		assertEquals("江南华都A区", strPY);
	}
	
	@Test
	public void testToDBC3(){
		String strCN = "南洋路９９６巷";
		String strPY = CnToSpell.toDBC(strCN);
		assertEquals("南洋路996巷", strPY);
	}
	
	@Test
	public void testSplitCnString(){
		String cnStr = "我是汉字";
		String[] arrayStr = CnToSpell.splitCnString(cnStr);
		assertEquals(4, arrayStr.length);
		assertEquals("我", arrayStr[0]);
		assertEquals("是", arrayStr[1]);
		assertEquals("汉", arrayStr[2]);
		assertEquals("字", arrayStr[3]);
		
		cnStr = "我是&带符%号的（汉字";
		arrayStr = CnToSpell.splitCnString(cnStr);
		assertEquals(8, arrayStr.length);
		assertEquals("我", arrayStr[0]);
		assertEquals("是", arrayStr[1]);
		assertEquals("带", arrayStr[2]);
		assertEquals("符", arrayStr[3]);
		assertEquals("号", arrayStr[4]);
		assertEquals("的", arrayStr[5]);
		assertEquals("汉", arrayStr[6]);
		assertEquals("字", arrayStr[7]);
		
		cnStr = "我是&带符%号跟英SA文js的（汉字";
		arrayStr = CnToSpell.splitCnString(cnStr);
		assertEquals(11, arrayStr.length);
		assertEquals("我", arrayStr[0]);
		assertEquals("是", arrayStr[1]);
		assertEquals("带", arrayStr[2]);
		assertEquals("符", arrayStr[3]);
		assertEquals("号", arrayStr[4]);
		assertEquals("跟", arrayStr[5]);
		assertEquals("英", arrayStr[6]);
		assertEquals("文", arrayStr[7]);
		assertEquals("的", arrayStr[8]);
		assertEquals("汉", arrayStr[9]);
		assertEquals("字", arrayStr[10]);
	}
	
	@Test
	public void testSplitCnString2(){
		String cnStr = "fgtewqw";
		String[] arrayStr = CnToSpell.splitCnString(cnStr);
		assertNull(arrayStr);
	}
	
	@Test
	public void testSplitCnString3(){
		String cnStr = "西湖A区";
		String[] arrayStr = CnToSpell.splitCnString(cnStr);
		assertEquals("西", arrayStr[0]);
		assertEquals("湖", arrayStr[1]);
		assertEquals("a", arrayStr[2]);
		assertEquals("区", arrayStr[3]);
	}
}
