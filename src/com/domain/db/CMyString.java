package com.domain.db;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;

public class CMyString
{
	public static String ENCODING_DEFAULT = "UTF-8";
	public static String GET_ENCODING_DEFAULT = "UTF-8";
	public static String FILE_WRITING_ENCODING = "GBK";
	private static Hashtable<?, ?> m_hCharName = null;
	public static boolean isEmpty(String _string)
	{
		return (_string == null) || (_string.trim().length() == 0);
	}


	public static String showObjNull(Object p_sValue)
	{
		return showObjNull(p_sValue, "");
	}

	public static String showObjNull(Object _sValue, String _sReplaceIfNull)
	{
	    if (_sValue == null)
	    	return _sReplaceIfNull;
	    return _sValue.toString();
	}

	public static String showNull(String p_sValue)
	{
		return showNull(p_sValue, "");
	}

	public static String showNull(String _sValue, String _sReplaceIfNull)
	{
		return _sValue == null ? _sReplaceIfNull : _sValue;
	}

	public static String expandStr(String _string, int _length, char _chrFill, boolean _bFillOnLeft)
	{
	    int nLen = _string.length();
	    if ( _length <= nLen )
	    {
	    	return _string;
	    }

	    String sRet = _string;
	    for ( int i = 0; i < _length - nLen; i++ )
	    {
	    	sRet = sRet + _chrFill;
	    }
	    return sRet;
	}

	public static String setStrEndWith(String _string, char _chrEnd)
	{
		if (_string == null)
			return null;
		if (_string.charAt(_string.length() - 1) != _chrEnd)
			return _string + _chrEnd;
		return _string;
	}

	public static String makeBlanks(int _length)
	{
	    if (_length < 1)
	    	return "";
	    StringBuffer buffer = new StringBuffer(_length);
	    for ( int i = 0; i < _length; i++ )
	    {
	    	buffer.append(' ');
	    }
	    return buffer.toString();
	}

  public static String replaceStr(String _strSrc, String _strOld, String _strNew)
  {
    if ((_strSrc == null) || (_strNew == null) || (_strOld == null)) {
      return _strSrc;
    }

    char[] srcBuff = _strSrc.toCharArray();
    int nSrcLen = srcBuff.length;
    if (nSrcLen == 0) {
      return "";
    }

    char[] oldStrBuff = _strOld.toCharArray();
    int nOldStrLen = oldStrBuff.length;
    if ((nOldStrLen == 0) || (nOldStrLen > nSrcLen)) {
      return _strSrc;
    }
    StringBuffer retBuff = new StringBuffer(nSrcLen * (1 +
      _strNew.length() /
      nOldStrLen));

    boolean bIsFound = false;

    int i = 0;
    while (i < nSrcLen) {
      bIsFound = false;

      if (srcBuff[i] == oldStrBuff[0]) {
        for (int j = 1; j < nOldStrLen; j++) {
          if (i + j >= nSrcLen)
            break;
          if (srcBuff[(i + j)] != oldStrBuff[j])
            break;
        }
        bIsFound = i == nOldStrLen;
      }

      if (bIsFound) {
        retBuff.append(_strNew);
        i += nOldStrLen;
      }
      else
      {
        int nSkipTo;
        if (i + nOldStrLen >= nSrcLen)
          nSkipTo = nSrcLen - 1;
        else {
          nSkipTo = i;
        }
        for (; i <= nSkipTo; i++) {
          retBuff.append(srcBuff[i]);
        }
      }
    }
    srcBuff = (char[])null;
    oldStrBuff = (char[])null;
    return retBuff.toString();
  }

  public static String replaceStr(StringBuffer _strSrc, String _strOld, String _strNew)
  {
    if (_strSrc == null) {
      return null;
    }

    int nSrcLen = _strSrc.length();
    if (nSrcLen == 0) {
      return "";
    }

    char[] oldStrBuff = _strOld.toCharArray();
    int nOldStrLen = oldStrBuff.length;
    if ((nOldStrLen == 0) || (nOldStrLen > nSrcLen)) {
      return _strSrc.toString();
    }
    StringBuffer retBuff = new StringBuffer(nSrcLen * (1 +
      _strNew.length() /
      nOldStrLen));

    boolean bIsFound = false;

    int i = 0;
    while (i < nSrcLen) {
      bIsFound = false;

      if (_strSrc.charAt(i) == oldStrBuff[0]) {
        for (int j = 1; j < nOldStrLen; j++) {
          if (i + j >= nSrcLen)
            break;
          if (_strSrc.charAt(i + j) != oldStrBuff[j])
            break;
        }
        bIsFound = (i == nOldStrLen);
      }

      if (bIsFound) {
        retBuff.append(_strNew);
        i += nOldStrLen;
      }
      else
      {
        int nSkipTo;
        if (i + nOldStrLen >= nSrcLen)
          nSkipTo = nSrcLen - 1;
        else {
          nSkipTo = i;
        }
        for (; i <= nSkipTo; i++) {
          retBuff.append(_strSrc.charAt(i));
        }
      }
    }
    oldStrBuff = (char[])null;
    return retBuff.toString();
  }

  public static String getStr(String _strSrc)
  {
    return getStr(_strSrc, ENCODING_DEFAULT);
  }

  public static String getStr(String _strSrc, boolean _bPostMethod)
  {
    return getStr(_strSrc, _bPostMethod ? ENCODING_DEFAULT :
      GET_ENCODING_DEFAULT);
  }

  public static String getStr(String _strSrc, String _encoding)
  {
    if ((_encoding == null) || (_encoding.length() == 0))
      return _strSrc;
    try
    {
      byte[] byteStr = new byte[_strSrc.length()];
      char[] charStr = _strSrc.toCharArray();
      for (int i = byteStr.length - 1; i >= 0; i--) {
        byteStr[i] = (byte)charStr[i];
      }

      return new String(byteStr, _encoding);
    }
    catch (Exception ex)
    {
    }

    return _strSrc;
  }

  public static String toISO_8859(String _strSrc)
  {
    if (_strSrc == null)
      return null;
    try
    {
      return new String(_strSrc.getBytes(), "ISO-8859-1"); } catch (Exception ex) {
    }
    return _strSrc;
  }

  /** @deprecated */
  public static String toUnicode(String _strSrc)
  {
    return toISO_8859(_strSrc);
  }

  public static byte[] getUTF8Bytes(String _string)
  {
    char[] c = _string.toCharArray();
    int len = c.length;

    int count = 0;
    for (int i = 0; i < len; i++) {
      int ch = c[i];
      if (ch <= 127)
        count++;
      else if (ch <= 2047)
        count += 2;
      else {
        count += 3;
      }

    }

    byte[] b = new byte[count];
    int off = 0;
    for (int i = 0; i < len; i++) {
      int ch = c[i];
      if (ch <= 127) {
        b[(off++)] = (byte)ch;
      } else if (ch <= 2047) {
        b[(off++)] = (byte)(ch >> 6 | 0xC0);
        b[(off++)] = (byte)(ch & 0x3F | 0x80);
      } else {
        b[(off++)] = (byte)(ch >> 12 | 0xE0);
        b[(off++)] = (byte)(ch >> 6 & 0x3F | 0x80);
        b[(off++)] = (byte)(ch & 0x3F | 0x80);
      }
    }
    return b;
  }

  public static String getUTF8String(byte[] b)
  {
    return getUTF8String(b, 0, b.length);
  }

  public static String getUTF8String(byte[] b, int off, int len)
  {
    int count = 0;
    int max = off + len;
    int i = off;
    while (i < max) {
      int c = b[(i++)] & 0xFF;
      switch (c >> 4)
      {
      case 0:
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
        count++;
        break;
      case 12:
      case 13:
        if ((b[(i++)] & 0xC0) != 128) {
          throw new IllegalArgumentException();
        }
        count++;
        break;
      case 14:
        if (((b[(i++)] & 0xC0) != 128) || ((b[(i++)] & 0xC0) != 128)) {
          throw new IllegalArgumentException();
        }
        count++;
        break;
      case 8:
      case 9:
      case 10:
      case 11:
      default:
        throw new IllegalArgumentException();
      }
    }
    if (i != max) {
      throw new IllegalArgumentException();
    }

    char[] cs = new char[count];
    i = 0;
    while (off < max) {
      int c = b[(off++)] & 0xFF;
      switch (c >> 4)
      {
      case 0:
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
        cs[(i++)] = (char)c;
        break;
      case 12:
      case 13:
        cs[(i++)] = (char)((c & 0x1F) << 6 | b[(off++)] & 0x3F);
        break;
      case 14:
        int t = (b[(off++)] & 0x3F) << 6;
        cs[(i++)] = (char)((c & 0xF) << 12 | t | b[(off++)] & 0x3F);
        break;
      case 8:
      case 9:
      case 10:
      case 11:
      default:
        throw new IllegalArgumentException();
      }
    }
    return new String(cs, 0, count);
  }

  public static String byteToHexString(byte[] _bytes)
  {
    return byteToHexString(_bytes, ',');
  }

  public static String byteToHexString(byte[] _bytes, char _delim)
  {
    String sRet = "";
    for (int i = 0; i < _bytes.length; i++) {
      if (i > 0) {
        sRet = sRet + _delim;
      }
      sRet = sRet + Integer.toHexString(_bytes[i]);
    }
    return sRet;
  }

  public static String byteToString(byte[] _bytes, char _delim, int _radix)
  {
    String sRet = "";
    for (int i = 0; i < _bytes.length; i++) {
      if (i > 0) {
        sRet = sRet + _delim;
      }
      sRet = sRet + Integer.toString(_bytes[i], _radix);
    }
    return sRet;
  }

  public static String transDisplay(String _sContent)
  {
    return transDisplay(_sContent, true);
  }

  public static String transDisplay(String _sContent, boolean _bChangeBlank)
  {
    if (_sContent == null) {
      return "";
    }
    char[] srcBuff = _sContent.toCharArray();
    int nSrcLen = srcBuff.length;

    StringBuffer retBuff = new StringBuffer(nSrcLen * 2);

    for (int i = 0; i < nSrcLen; i++) {
      char cTemp = srcBuff[i];
      switch (cTemp) {
      case ' ':
        retBuff.append(_bChangeBlank ? "&nbsp;" : " ");
        break;
      case '<':
        retBuff.append("&lt;");
        break;
      case '>':
        retBuff.append("&gt;");
        break;
      case '\n':
        retBuff.append("<br>");
        break;
      case '"':
        retBuff.append("&quot;");
        break;
      case '&':
        boolean bUnicode = false;
        for (int j = i + 1; (j < nSrcLen) && (!bUnicode); j++) {
          cTemp = srcBuff[j];
          if ((cTemp == '#') || (cTemp == ';')) {
            retBuff.append("&");
            bUnicode = true;
          }
        }
        if (bUnicode) continue;
        retBuff.append("&amp;");
        break;
      case '\t':
        retBuff.append(_bChangeBlank ? "&nbsp;&nbsp;&nbsp;&nbsp;" :
          "    ");
        break;
      default:
        retBuff.append(cTemp);
      }
    }
    return retBuff.toString();
  }

  public static String transDisplay_bbs(String _sContent, String p_sQuoteColor)
  {
    return transDisplay_bbs(_sContent, p_sQuoteColor, true);
  }

  public static String transDisplay_bbs(String _sContent, String p_sQuoteColor, boolean _bChangeBlank)
  {
    if (_sContent == null) {
      return "";
    }

    boolean bIsQuote = false;
    boolean bIsNewLine = true;

    char[] srcBuff = _sContent.toCharArray();
    int nSrcLen = srcBuff.length;

    StringBuffer retBuff = new StringBuffer((int)(nSrcLen * 1.8D));

    for (int i = 0; i < nSrcLen; i++) {
      char cTemp = srcBuff[i];
      switch (cTemp) {
      case ':':
        if (bIsNewLine) {
          bIsQuote = true;
          retBuff.append("<font color=" + p_sQuoteColor + ">:");
        } else {
          retBuff.append(":");
        }
        bIsNewLine = false;
        break;
      case ' ':
        retBuff.append(_bChangeBlank ? "&nbsp;" : " ");
        bIsNewLine = false;
        break;
      case '<':
        retBuff.append("&lt;");
        bIsNewLine = false;
        break;
      case '>':
        retBuff.append("&gt;");
        bIsNewLine = false;
        break;
      case '"':
        retBuff.append("&quot;");
        bIsNewLine = false;
        break;
      case '&':
        retBuff.append("&amp;");
        bIsNewLine = false;
        break;
      case '\t':
        retBuff.append(_bChangeBlank ? "&nbsp;&nbsp;&nbsp;&nbsp;" :
          "    ");
        bIsNewLine = false;
        break;
      case '\n':
        if (bIsQuote) {
          bIsQuote = false;
          retBuff.append("</font>");
        }
        retBuff.append("<br>");
        bIsNewLine = true;
        break;
      default:
        retBuff.append(cTemp);
        bIsNewLine = false;
      }
    }

    if (bIsQuote) {
      retBuff.append("</font>");
    }
    return retBuff.toString();
  }

  public static String transJsDisplay(String _sContent)
  {
    if (_sContent == null) {
      return "";
    }
    char[] srcBuff = _sContent.toCharArray();
    int nSrcLen = srcBuff.length;

    StringBuffer retBuff = new StringBuffer((int)(nSrcLen * 1.5D));

    for (int i = 0; i < nSrcLen; i++) {
      char cTemp = srcBuff[i];
      switch (cTemp) {
      case '<':
        retBuff.append("&lt;");
        break;
      case '>':
        retBuff.append("&gt;");
        break;
      case '"':
        retBuff.append("&quot;");
        break;
      default:
        retBuff.append(cTemp);
      }
    }
    return retBuff.toString();
  }

  public static String transDisplayMark(String _strSrc, char p_chrMark)
  {
    if (_strSrc == null) {
      return "";
    }

    char[] buff = new char[_strSrc.length()];
    for (int i = 0; i < buff.length; i++) {
      buff[i] = p_chrMark;
    }
    return new String(buff);
  }

  public static String filterForSQL(String _sContent)
  {
    if (_sContent == null) {
      return "";
    }
    int nLen = _sContent.length();
    if (nLen == 0) {
      return "";
    }
    char[] srcBuff = _sContent.toCharArray();
    StringBuffer retBuff = new StringBuffer((int)(nLen * 1.5D));

    for (int i = 0; i < nLen; i++) {
      char cTemp = srcBuff[i];
      switch (cTemp) {
      case '\'':
        retBuff.append("''");
        break;
      case ';':
        boolean bSkip = false;
        for (int j = i + 1; (j < nLen) && (!bSkip); j++) {
          char cTemp2 = srcBuff[j];
          if (cTemp2 == ' ')
            continue;
          if (cTemp2 == '&')
            retBuff.append(';');
          bSkip = true;
        }
        if (bSkip) continue;
        retBuff.append(';');
        break;
      default:
        retBuff.append(cTemp);
      }

    }

    return retBuff.toString();
  }

  public static String filterForXML(String _sContent)
  {
    if (_sContent == null) {
      return "";
    }
    char[] srcBuff = _sContent.toCharArray();
    int nLen = srcBuff.length;
    if (nLen == 0) {
      return "";
    }
    StringBuffer retBuff = new StringBuffer((int)(nLen * 1.8D));

    for (int i = 0; i < nLen; i++) {
      char cTemp = srcBuff[i];
      switch (cTemp) {
      case '&':
        retBuff.append("&amp;");
        break;
      case '<':
        retBuff.append("&lt;");
        break;
      case '>':
        retBuff.append("&gt;");
        break;
      case '"':
        retBuff.append("&quot;");
        break;
      case '\'':
        retBuff.append("&apos;");
        break;
      default:
        retBuff.append(cTemp);
      }
    }

    return retBuff.toString();
  }

  public static String filterForHTMLValue(String _sContent)
  {
    if (_sContent == null) {
      return "";
    }

    char[] srcBuff = _sContent.toCharArray();
    int nLen = srcBuff.length;
    if (nLen == 0) {
      return "";
    }
    StringBuffer retBuff = new StringBuffer((int)(nLen * 1.8D));

    for (int i = 0; i < nLen; i++) {
      char cTemp = srcBuff[i];
      switch (cTemp)
      {
      case '&':
        if (i + 1 < nLen) {
          cTemp = srcBuff[(i + 1)];
          if (cTemp == '#')
            retBuff.append("&");
          else
            retBuff.append("&amp;");
        } else {
          retBuff.append("&amp;");
        }break;
      case '<':
        retBuff.append("&lt;");
        break;
      case '>':
        retBuff.append("&gt;");
        break;
      case '"':
        retBuff.append("&quot;");
        break;
      default:
        retBuff.append(cTemp);
      }
    }

    return retBuff.toString();
  }

  public static String filterForUrl(String _sContent)
  {
    if (_sContent == null) {
      return "";
    }
    char[] srcBuff = _sContent.toCharArray();
    int nLen = srcBuff.length;
    if (nLen == 0) {
      return "";
    }
    StringBuffer retBuff = new StringBuffer((int)(nLen * 1.8D));

    for (int i = 0; i < nLen; i++) {
      char cTemp = srcBuff[i];
      switch (cTemp) {
      case '%':
        retBuff.append("%25");
        break;
      case '?':
        retBuff.append("%3F");
        break;
      case '#':
        retBuff.append("%23");
        break;
      case '&':
        retBuff.append("%26");
        break;
      case ' ':
        retBuff.append("%20");
        break;
      default:
        retBuff.append(cTemp);
      }
    }

    return retBuff.toString();
  }

  public static String filterForJs(String _sContent)
  {
    if (_sContent == null) {
      return "";
    }
    char[] srcBuff = _sContent.toCharArray();
    int nLen = srcBuff.length;
    if (nLen == 0) {
      return "";
    }
    StringBuffer retBuff = new StringBuffer((int)(nLen * 1.8D));

    for (int i = 0; i < nLen; i++) {
      char cTemp = srcBuff[i];
      switch (cTemp) {
      case '"':
        retBuff.append("\\\"");
        break;
      case '\\':
        retBuff.append("\\\\");
        break;
      case '\n':
        retBuff.append("\\n");
        break;
      case '\r':
        retBuff.append("\\r");
        break;
      case '\f':
        retBuff.append("\\f");
        break;
      case '\t':
        retBuff.append("\\t");
        break;
      case '/':
        retBuff.append("\\/");
        break;
      default:
        retBuff.append(cTemp);
      }
    }

    return retBuff.toString();
  }

  public static String numberToStr(int _nValue)
  {
    return numberToStr(_nValue, 0);
  }

  public static String numberToStr(int _nValue, int _length)
  {
    return numberToStr(_nValue, _length, '0');
  }

  public static String numberToStr(int _nValue, int _length, char _chrFill)
  {
    String sValue = String.valueOf(_nValue);
    return expandStr(sValue, _length, _chrFill, true);
  }

  public static String numberToStr(long _lValue)
  {
    return numberToStr(_lValue, 0);
  }

  public static String numberToStr(long _lValue, int _length)
  {
    return numberToStr(_lValue, _length, '0');
  }

  public static String numberToStr(long _lValue, int _length, char _chrFill)
  {
    String sValue = String.valueOf(_lValue);
    return expandStr(sValue, _length, _chrFill, true);
  }

  public static String circleStr(String _strSrc)
  {
    if (_strSrc == null) {
      return null;
    }
    String sResult = "";
    int nLength = _strSrc.length();
    for (int i = nLength - 1; i >= 0; i--) {
      sResult = sResult + _strSrc.charAt(i);
    }
    return sResult;
  }

  public static final boolean isChineseChar(int c)
  {
    return c > 127;
  }

  public static final int getCharViewWidth(int c)
  {
    return isChineseChar(c) ? 2 : 1;
  }

  public static final int getStringViewWidth(String s)
  {
    if ((s == null) || (s.length() == 0)) {
      return 0;
    }

    int iWidth = 0;
    int iLength = s.length();

    for (int i = 0; i < iLength; i++) {
      iWidth += getCharViewWidth(s.charAt(i));
    }

    return iWidth;
  }

  public static String truncateStr(String _string, int _maxLength)
  {
    return truncateStr(_string, _maxLength, "..");
  }

  public static String truncateStr(String _string, int _maxLength, String _sExt)
  {
    if (_string == null) {
      return null;
    }

    if (_sExt == null) {
      _sExt = "..";
    }

    int nSrcLen = getStringViewWidth(_string);
    if (nSrcLen <= _maxLength)
    {
      return _string;
    }

    int nExtLen = getStringViewWidth(_sExt);
    if (nExtLen >= _maxLength)
    {
      return _string;
    }

    int iLength = _string.length();
    int iRemain = _maxLength - nExtLen;
    StringBuffer sb = new StringBuffer(_maxLength + 2);

    for (int i = 0; i < iLength; i++) {
      char aChar = _string.charAt(i);
      int iNeed = getCharViewWidth(aChar);
      if (iNeed > iRemain) {
        sb.append(_sExt);
        break;
      }
      sb.append(aChar);
      iRemain -= iNeed;
    }

    return sb.toString();
  }

  public static String filterForJDOM(String _string)
  {
    if (_string == null) {
      return null;
    }
    char[] srcBuff = _string.toCharArray();
    int nLen = srcBuff.length;

    StringBuffer dstBuff = new StringBuffer(nLen);

    for (int i = 0; i < nLen; i++) {
      char aChar = srcBuff[i];
      if (!isValidCharOfXML(aChar)) {
        continue;
      }
      dstBuff.append(aChar);
    }
    return dstBuff.toString();
  }

  public static boolean isValidCharOfXML(char _char)
  {
    return (_char == '\t') || (_char == '\n') || (_char == '\r') ||
      ((' ' <= _char) && (_char <= 55295)) ||
      ((57344 <= _char) && (_char <= 65533)) || (
      (65536 <= _char) && (_char <= 1114111));
  }

  public static int getBytesLength(String _string)
  {
    if (_string == null) {
      return 0;
    }
    char[] srcBuff = _string.toCharArray();

    int nGet = 0;
    for (int i = 0; i < srcBuff.length; i++) {
      char aChar = srcBuff[i];
      nGet += (aChar <= '' ? 1 : 2);
    }
    return nGet;
  }

  /** @deprecated */
  public static String cutStr(String _string, int _length)
  {
    return truncateStr(_string, _length);
  }

  public static String URLEncode(String s)
  {

    return s;
  }

  public static String[] split(String _str, String _sDelim)
  {
    if ((_str == null) || (_sDelim == null)) {
      return new String[0];
    }

    StringTokenizer stTemp = new StringTokenizer(_str,
      _sDelim);
    int nSize = stTemp.countTokens();
    if (nSize == 0) {
      return new String[0];
    }

    String[] str = new String[nSize];
    int i = 0;
    while (stTemp.hasMoreElements()) {
      str[i] = stTemp.nextToken();
      i++;
    }
    return str;
  }

  public static int countTokens(String _str, String _sDelim)
  {
    StringTokenizer stTemp = new StringTokenizer(_str,
      _sDelim);
    return stTemp.countTokens();
  }

  public static int[] splitToInt(String _str, String _sDelim)
  {
    if (isEmpty(_str)) {
      return new int[0];
    }

    if (isEmpty(_sDelim)) {
      _sDelim = ",";
    }

    StringTokenizer stTemp = new StringTokenizer(_str,
      _sDelim);
    int[] arInt = new int[stTemp.countTokens()];
    int nIndex = 0;

    while (stTemp.hasMoreElements()) {
      String sValue = (String)stTemp.nextElement();
      arInt[nIndex] = Integer.parseInt(sValue.trim());
      nIndex++;
    }
    return arInt; }
  // ERROR //
  @SuppressWarnings("unused")
  private static void loadFirstLetter(String _sFileName) throws Exception {

  }
  private static Hashtable<?, ?> getPYResource() throws Exception {
	  if (m_hCharName != null) {
      return m_hCharName;
    }

//    String sResourcePath = CMyFile.mapResouceFullPath(PY_RESOURCE_FILE);
//    loadFirstLetter(sResourcePath);
    return m_hCharName;
  }

  public static String getFirstLetter(String _str) throws Exception
  {
    if ((_str == null) || (_str.length() < 0)) {
      return "";
    }
    char[] arChars = _str.toCharArray();
    String sFirstChar = _str.substring(0, 1);
    if (arChars[0] > '') {
      return ((String)getPYResource().get(sFirstChar.toUpperCase()))
        .toUpperCase();
    }
    return sFirstChar.toUpperCase();
  }

  public static final String encodeForCDATA(String _str)
  {
    if ((_str == null) || (_str.length() < 1)) {
      return _str;
    }

    return replaceStr(_str, "]]>", "(DOMAIN_CDATA_END_HOLDER_CMS)");
  }

  public static final String decodeForCDATA(String _str)
  {
    if ((_str == null) || (_str.length() < 1)) {
      return _str;
    }

    return replaceStr(_str, "(DOMAIN_CDATA_END_HOLDER_CMS)", "]]>");
  }

  public static final boolean isContainChineseChar(String _str)
  {
    if (_str == null) {
      return false;
    }

    return _str.getBytes().length != _str.length();
  }

  public static String join(ArrayList<?> _arColl, String _sSeparator)
  {
    if (_arColl == null) {
      return null;
    }

    return join(_arColl.toArray(), _sSeparator);
  }

  public static String join(Object[] _arColl, String _sSeparator)
  {
    if ((_arColl == null) || (_arColl.length == 0) || (_sSeparator == null)) {
      return null;
    }
    if (_arColl.length == 1) {
      return _arColl[0].toString();
    }

    StringBuffer result = new StringBuffer(_arColl[0].toString());
    for (int i = 1; i < _arColl.length; i++) {
      result.append(_sSeparator);
      result.append(_arColl[i].toString());
    }

    return result.toString();
  }
}

