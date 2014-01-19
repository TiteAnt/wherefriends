package com.tite.system.comfc.utility;

import java.util.regex.Pattern;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 汉字转化为全拼,本工具类对第三方组件进行了封装<br />
 * 在将指定的汉字转换为拼音前,先清除了字符串中的拼音与特殊符号,<br />
 * 并将数字单独分离, 然后再调用第三方组件进行了转拼音操作
 * 
 * @author jiaoyk 2012-09-12
 * @version DFC 6.5.0
 */
public class CnToSpell {
	//移除所有英文、特殊符号的正则表达式
	private final static Pattern PATTERN_REMOVE_1 = Pattern.compile("[a-zA-Z^~`@#!·、“”%&*(){￥}|’,<>;-=?$ ]+");
	//移除特殊符号的正则表达式
	private final static Pattern PATTERN_REMOVE_2 = Pattern.compile("[ ^~`@#!·、“”%&*(){￥}|’,<>;-=?$ ]+");
	//移除所有连续的英文字母,保留单字母(主要用于地址匹配中词典的拼音转换)
	private final static Pattern PATTERN_REMOVE_3 = Pattern.compile("\\w+\\w+");
	private final static Pattern PATTERN_REMOVE_NUMBER = Pattern.compile("[0-9]+");
	/**
	 * 将汉字转换为拼音,拼音之间以逗号分隔,每个字的拼音均是由"拼音+声调"构成<br />
	 * 如"重庆市"转换后为"chong2,qing4,shi4"
	 * @param cn 汉字字符串
	 * @return String
	 * */
	public static String getFullSpell(String cn){
		//中文全角转半角
		cn = toDBC(cn);
		//将数字转换为大写"一""二"等
		cn = stringNumUppercase(cn);
		//清除字符串中的英文字符、特殊符号、数字以及空格
		cn = PATTERN_REMOVE_1.matcher(cn).replaceAll("");
		//如果发送的字符串完全是由英文与字符串构成的, 则不存在拼音
		if("".equalsIgnoreCase(cn.trim())){
			return cn;
		}
		//将汉字转换为拼音
		char[] nameChar = cn.toCharArray();  
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();  
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);  
        StringBuffer sbPY = new StringBuffer();
        for (int i = 0; i < nameChar.length; i++) {  
            if (nameChar[i] > 128) {  
                try {  
                	sbPY.append(PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0]);  
                } catch (BadHanyuPinyinOutputFormatCombination e) {  
                    continue;  
                }  
            }else{  
            	//若无法转换为拼音,则记录空缺
        		sbPY.append("null");
                continue;
            }  
            if(i < nameChar.length-1){
            	sbPY.append(",");
            }
        }
		return sbPY.toString();
	}
	
	/**
	 * 将汉字转换为拼音,拼音之间以逗号分隔,每个字的拼音不带声调<br />
	 * 如"重庆市"转换后为"chong,qing,shi"
	 * @param cn 汉字字符串
	 * @return String
	 * */
	public static String getFullSpell2(String cn){
		//中文全角转半角
		cn = toDBC(cn);
		//将数字转换为大写"一""二"等
		cn = stringNumUppercase(cn);
		//清除字符串中的特殊符号以及空格
		cn = PATTERN_REMOVE_2.matcher(cn).replaceAll("");
		//清除字符串中的连续英文字符
		cn = PATTERN_REMOVE_3.matcher(cn).replaceAll("");
		//全部转小写
		cn = cn.toLowerCase();
		//如果发送的字符串完全是由英文与字符串构成的, 则不存在拼音
		if("".equalsIgnoreCase(cn.trim())){
			return cn;
		}
		//将汉字转换为拼音
		char[] nameChar = cn.toCharArray();  
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();  
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);  
        StringBuffer sbPY = new StringBuffer();
        for (int i = 0; i < nameChar.length; i++) {  
            if (nameChar[i] > 128) {  
                try {
                	if(PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat) != null){
                		sbPY.append(PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0]);
                	}else{
                		//若无法转换为拼音,则记录空缺
                		sbPY.append("null");
                	}
                } catch (BadHanyuPinyinOutputFormatCombination e) {  
                	//若无法转换为拼音,则记录空缺
            		sbPY.append("null");
                    continue;
                }  
            }else{  
            	sbPY.append(nameChar[i]);  
            }  
            if(i < nameChar.length-1){
            	sbPY.append(",");
            }
        }
		return PATTERN_REMOVE_NUMBER.matcher(sbPY.toString()).replaceAll("");
	}
	
	/**
	 * 将字符串中的数字转换为大写"一","二"等
	 * @param string 字符串
	 * */
	private static String stringNumUppercase(String string){
		string = string.replace("0", "零");
		string = string.replace("1", "一");
		string = string.replace("2", "二");
		string = string.replace("3", "三");
		string = string.replace("4", "四");
		string = string.replace("5", "五");
		string = string.replace("6", "六");
		string = string.replace("7", "七");
		string = string.replace("8", "八");
		string = string.replace("9", "九");
		return string;
	}
	
	/**
	 * 全角转半角
	 * @param input 需要转换的全角字符串
	 * @return 转换成的半角字符串
	 */
	public static String toDBC(String _input)
	{
		char[] _c = _input.toCharArray();
		for (int i = 0; i < _c.length; i++)
		{
			if (_c[i] == 12288)
			{
				_c[i] = (char) 32;
				continue;
			}
			if (_c[i] > 65280 && _c[i] < 65375)
				_c[i] = (char) (_c[i] - 65248);
		}

		// 返回转换后的半角字符
		return new String(_c);
	}
	
	/**
	 * 将汉字字符串转换为数组, 转换前将字符串中的连续英文以及特殊符号移除
	 * @param cnStr 汉字字符串
	 * @return String[]
	 * */
	public static String[] splitCnString(String cnStr){
		//中文全角转半角
		cnStr = toDBC(cnStr);
		//将数字转换为大写"一""二"等
		cnStr = stringNumUppercase(cnStr);
		//清除字符串中的英文字符、特殊符号、数字以及空格
		cnStr = PATTERN_REMOVE_2.matcher(cnStr).replaceAll("");
		//清除字符串中的连续英文字符
		cnStr = PATTERN_REMOVE_3.matcher(cnStr).replaceAll("");
		//全部转小写
		cnStr = cnStr.toLowerCase();
		//如果发送的字符串完全是由英文与字符串构成的, 则无法分组
		if("".equalsIgnoreCase(cnStr.trim())){
			return null;
		}
		int length = cnStr.length();
		String[] arrayStr = new String[length];
		for(int i=0; i<length; i++){
			arrayStr[i] = cnStr.substring(i, i+1);
		}
		
		return arrayStr;
	}
}
