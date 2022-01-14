import java.io.UnsupportedEncodingException;

/**
 * 트립 파일 정보 Byte to String 및 관리 Class
 */
public class ByteString
{

    //
    public static final String EMPTY_STRING = "";

    //
    private byte[] m_byData;
    private int m_nDataSize = 0;

    public ByteString(String sData)
    {
        try
        {
            if (sData != null && sData.length() > 0)
            {
                m_byData = sData.getBytes("EUC-KR");
                m_nDataSize = m_byData.length;
            }
        }
        catch (UnsupportedEncodingException e)
        {
            m_byData = null;
        }
        if (m_byData == null) m_byData = new byte[1];
    }

    //
    public ByteString(int allocSize)
    {
        m_byData = new byte[allocSize];
    }

    //
    public void setData(byte[] data, int offset, int count)
    {
        m_nDataSize = Math.min(m_byData.length, count);
        if (m_nDataSize > 0) System.arraycopy(data, offset, m_byData, 0, m_nDataSize);
    }

    public void setData(byte[] byData, int nPos, int nSize, byte chTrim)
    {
        int nOffset;
        int nDataSize = 0;
        int nEndPos = nPos + nSize - 1;
        int nDatPos = 0;

        nOffset = nEndPos;
        for (int i = nPos; i < nPos + nSize; i++)
        {
            if (byData[i] != chTrim)
            {
                nOffset = i;
                break;
            }
        }

        if (nOffset >= nEndPos)
        {
            m_nDataSize = 0;
            return;
        }

        nDataSize = (nEndPos - nOffset) + 1;
        for (int e = nEndPos; e >= nOffset; e--)
        {
            if (byData[e] != chTrim)
            {
                nDataSize = (e - nOffset) + 1;
                break;
            }
        }

        nDataSize = Math.min(m_byData.length, nDataSize);
        for (int n = 0; n < nDataSize; n++)
        {
            if (byData[nOffset + n] == '\r' || byData[nOffset + n] == '\n') continue;
            m_byData[nDatPos] = byData[nOffset + n];
            nDatPos++;
        }
        m_nDataSize = nDatPos;
    }

    public void setData_CarNum(byte[] byData, int nPos, int nSize, byte chTrim)
    {
        int nOffset;
        int nDataSize = 0;
        int nEndPos = nPos + nSize - 1;

        nOffset = nEndPos;
        for (int i = nPos; i < nPos + nSize; i++)
        {
            if (byData[i] != chTrim)
            {
                nOffset = i;
                break;
            }
        }

        if (nOffset >= nEndPos)
        {
            m_nDataSize = 0;
            return;
        }

        nDataSize = (nEndPos - nOffset) + 1;
        for (int e = nEndPos; e >= nOffset; e--)
        {
            if (byData[e] != chTrim)
            {
                nDataSize = (e - nOffset) + 1;
                break;
            }
        }

        m_nDataSize = setData_CarRegNum(byData, nOffset, nDataSize);
    }

    public void clear()
    {
        m_nDataSize = 0;
    }

    public boolean isEmpty()
    {
        return m_nDataSize <= 0;
    }

    public byte charAt(int pos)
    {
        return m_byData[pos];
    }

    public byte[] getData()
    {
        return m_byData;
    }

    public int getSize()
    {
        return m_nDataSize;
    }

    //
    boolean isHangul(int nStart, int nLen)
    {
        int ch;
        for (int i = nStart; i < nStart + nLen; i++)
        {
            ch = m_byData[i] & 0x00ff;
            if (ch < 0x0080) return false;
        }
        return true;
    }

    public boolean isDigit(int pos)
    {
        return (m_byData[pos] >= 0x30 && m_byData[pos] <= 0x39);
    }

    public boolean isDigit(int nStart, int nLen)
    {
        for (int i = nStart; i < nStart + nLen; i++)
        {
            if (m_byData[i] < 0x30 || m_byData[i] > 0x39) return false;
        }
        return true;
    }

    public boolean isAlpha(int pos)
    {
        if (m_byData[pos] >= 'A' && m_byData[pos] <= 'Z') return true;
        return (m_byData[pos] >= 'a' && m_byData[pos] <= 'z');
    }


    //
    public int toInt()
    {
        return toInt(-1);
    }

    public int toInt(int nDefVal)
    {
        if (m_nDataSize <= 0) return nDefVal;

        int nValue = 0;
        int pow = 1;

        for (int i = m_nDataSize - 1; i >= 0; i--)
        {
            if (m_byData[i] < 0x30 || m_byData[i] > 0x39) return nDefVal;

            nValue += (m_byData[i] - 48) * pow;
            pow *= 10;
        }

        return nValue;
    }

    public int toInt(int nPos, int nSize)
    {
        int nValue = 0;
        int pow = 1;

        for (int i = nPos + nSize - 1; i >= nPos; i--)
        {
            if (m_byData[i] < 0x30 || m_byData[i] > 0x39) return -1;

            nValue += (m_byData[i] - 48) * pow;
            pow *= 10;
        }

        return nValue;
    }

    public String toStr()
    {
        if (m_nDataSize <= 0) return EMPTY_STRING;

        String sData = null;

        try
        {
            sData = new String(m_byData, 0, m_nDataSize, "EUC-KR");
        }
        catch (UnsupportedEncodingException e)
        {
        }

        if (sData == null) return EMPTY_STRING;

        return sData;
    }

    //
    @Override
    public String toString()
    {
        return toStr();
    }

    //
    private int setData_CarRegNum(byte[] byCarRegNum, int nPos, int nSize)
    {
        int nInx = nPos, nECount = nPos + nSize;
        int nNewPos = 0;

        while (nInx < nECount)
        {
            int ch = byCarRegNum[nInx] & 0x00ff;
            if (ch >= 0x0080)
            {
                if (nInx + 1 >= nECount) break;

                m_byData[nNewPos++] = byCarRegNum[nInx];
                nInx++;
                m_byData[nNewPos++] = byCarRegNum[nInx];
                nInx++;
            }
            else
            {
                if (ch >= 0x30 && ch <= 0x39) m_byData[nNewPos++] = byCarRegNum[nInx];
                nInx++;
            }
        }

        return nNewPos;
    }

}
