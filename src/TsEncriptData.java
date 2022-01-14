

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

//import kr.co.kti.moovedtg.dtgfile.ByteString;

/**
 * TS 복호화.
 * 규격서참조.
 *
 * @author kti.co.kr
 * @version 1.0.0
 */
public class TsEncriptData
{
    /**
     * File to InputStream.
     *
     * @param inputStream
     * @param sFileName
     * @return
     */
    public static InputStream decTripData(InputStream inputStream, String sFileName)
    {
        byte encKey = 0x01;
        long lnSize = 0;

        OutputStream os = null;
        InputStream is = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        byte byData[] = new byte[8192];
        int nRcnt;

        encKey = makeEncFileKey(sFileName);

        try
        {
            while (true)
            {
                nRcnt = inputStream.read(byData);
                if (nRcnt <= 0) break;

                for (int i = 0; i < nRcnt; i++)
                {
                    byData[i] ^= encKey;
                }

                byteArrayOutputStream.write(byData, 0, nRcnt);
                lnSize += nRcnt;
            }

            byte[] bytes = byteArrayOutputStream.toByteArray();
            is = new ByteArrayInputStream(bytes);
        }
        catch (IOException ioe)
        {
            System.out.println("TsEncriptData exception --> " + ioe.getLocalizedMessage());
        }
        catch (Exception e)
        {
        	System.out.println("TsEncriptData exception --> " + e.getLocalizedMessage());
        }
        finally
        {
            if (os != null)
            {
                try
                {
                    os.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

                os = null;
            }
        }

        return is;
    }

    /**
     * 복호화 Key 만들기
     *
     * @param sFileName
     * @return
     */
    private static byte makeEncFileKey(String sFileName)
    {
        byte chEncKey;
        int nSumData = 0;

        ByteString bstr = new ByteString(sFileName);

        for (int i = 0; i < bstr.getSize(); i++)
        {
            nSumData += (int) (bstr.charAt(i) & 0x00FF);
        }

        chEncKey = (byte) (nSumData & 0x000000FF);

        if (chEncKey == 0) chEncKey = 0x01;

        return chEncKey;
    }
}
