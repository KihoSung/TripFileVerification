import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.client.utils.CloneUtils;

import com.sun.corba.se.impl.ior.ByteBuffer;

/**
 * Trip File 내용 검증
 * 
 * @author 넷케이티아이
 *
 */
public class TripFileVerification
{
	/**
	 * Trip File 검증 Main Method
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		TripFileVerification tfv = new TripFileVerification();
		VerificationTripFile vtf = tfv.new VerificationTripFile();

		vtf.verificationTripFile();
	}

	/**
	 * Trip File 검증 Inner Class
	 * 
	 * @author 넷케이티아이
	 *
	 */
	class VerificationTripFile
	{
		/**
		 * Trip File 경로
		 */
		private final String STR_FILE_PATH = "C:\\PDS\\PC_PDS\\Data";

		private final String STR_ENC_STATUS = "E"; // P : 평문 , E : 암호화 //

		/**
		 * 검증 파일 자동차 번호
		 */
		private String strCarNo = "";

		/**
		 * 검증 파일 단말기 모델명
		 */
		private String strHndset = "";

		/**
		 * 검증 파일 파일명
		 */
		private String strFileName = "";

		private String strBeforeDate = "00000000000000";

		/**
		 * 검증 실행 Method
		 */
		public void verificationTripFile()
		{
			// 파일 목록 불러오기 //
			File listFile[] = this.getFileList(STR_FILE_PATH);

			try
			{
				this.verifyTripFile(listFile);
			}
			catch (CloneNotSupportedException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		/**
		 * 경로에 있는 파일 목록 불러오기
		 * 
		 * @param strFilePath
		 * @return
		 */
		private File[] getFileList(String strFilePath)
		{
			// this.writeLog("[getFileList] strFilePath : " + strFilePath);

			File f = new File(strFilePath);

			File listFile[] = f.listFiles();

			// this.writeLog("[getFileList] listFile.length : " + listFile.length);

			return listFile;
		}

		/**
		 * 파일 검증 시작
		 * 
		 * @param listFile
		 * @throws IOException
		 * @throws CloneNotSupportedException
		 */
		private void verifyTripFile(File listFile[]) throws IOException, CloneNotSupportedException
		{
			for (File file : listFile)
			{
				TripDataVO tdv = new TripDataVO();
				tdv.setStrFileName(file.getName());
				strFileName = file.getName();

				// this.writeLog(">>>>>>>>>>>>>>> File Start <<<<<<<<<<<<<<<<");

				strBefore = "00000000";

				// 초단위 데이터 객체 변경 //
				this.convertTripData(tdv, file);
			}
		}

		/**
		 * Trip File 운행 데이터 VO 생성 및 검증
		 * 
		 * @param tdv
		 * @param file
		 * @throws IOException
		 * @throws CloneNotSupportedException
		 */
		private void convertTripData(TripDataVO tdv, File file) throws IOException, CloneNotSupportedException
		{
			BufferedReader br;

			if (STR_ENC_STATUS.equals("E"))
			{
				br = new BufferedReader(new InputStreamReader(TsEncriptData.decTripData(new FileInputStream(file), file.getName()), "EUC-KR"));
			}
			else if (STR_ENC_STATUS.equals("P"))
			{
				br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "EUC-KR")); // 생성된 파일의 charset 형식에 맞게 불러오기 //
			}
			else
			{
				System.out.println("Encoding 구분 오류");
				return;
			}

			String strData = "";
			// String strDivData = "";
			int cur = 0;
			byte[] arrByte = null;

			while ((strData = br.readLine()) != null)
			{
				// this.writeLog(strData);
				tdv.setStrDriveData(strData);
				// strDivData = strData;
				arrByte = strData.getBytes();

				// this.writeLog(new String(arrByte));

				// System.out.println("cur:" + cur + "|strData:" + strData + "|length:" + strData.length());

				int intPos = 0;

				this.writeLog("길이:" + arrByte.length);

				while (intPos < arrByte.length)
				{
					if (cur == 0)
					{
						String strVData = new String(arrByte, 0, 80);
						
						this.writeLog(strVData);

						tdv.setStrCarNo(strVData.substring(39, 50).replace("#", ""));
						tdv.setStrHndsetModel(strVData.substring(0, 20).replace("#", ""));

						strCarNo = tdv.getStrCarNo();
						strHndset = tdv.getStrHndsetModel();

						this.writeLog(">>>>>>>>>>>>>>> File Verification Start <<<<<<<<<<<<<<<<");

						tdbv = null;
						strBeforMaxDriveTime = "";

						intPos += 80;
					}
					else
					{
						try
						{

							String strVData = new String(arrByte, intPos, 68);
							
							// this.writeLog(strVData);

							strCarNo = tdv.getStrCarNo();
							strHndset = tdv.getStrHndsetModel();

							// this.writeLog(strData.substring(0, 4) + "|" + strData.substring(4, 11) + "|" + strData.substring(11, 25) + "|" +
							// strData.substring(25, 28) + "|" + strData.substring(28, 32) + "|" + strData.substring(32, 33) + "|" +
							// strData.substring(33,
							// 42)
							// +
							// "|" + strData.substring(42, 51) + "|"
							// + strData.substring(51, 54) + "|" + strData.substring(54, 60) + "|" + strData.substring(60, 66) + "|" +
							// strData.substring(66,
							// 68));

							tdv.setStrDayDist(strVData.substring(0, 4));
							tdv.setStrAccuDist(strVData.substring(4, 11));
							tdv.setStrDriveTime(strVData.substring(11, 25));
							
							if (cur == 1)
							{
								this.writeLog("Start Date : " +  new SimpleDateFormat("yyMMddHHmmssSS").parse(tdv.getStrDriveTime()));
							}
							
							tdv.setStrSpeed(strVData.substring(25, 28));
							tdv.setStrRpm(strVData.substring(28, 32));
							tdv.setStrBreak(strVData.substring(32, 33));
							tdv.setStrXcrd(strVData.substring(33, 42));
							tdv.setStrYcrd(strVData.substring(42, 51));
							tdv.setStrRd(strVData.substring(51, 54));
							tdv.setStrAccelVx(strVData.substring(54, 60));
							tdv.setStrAccelVy(strVData.substring(60, 66));
							tdv.setStrStatus(strVData.substring(66, 68));

							// 초단위 라인 운행데이터 검증 //
							this.verifyDriveData(tdv);

							if (Long.parseLong(strBefore) > 0)
							{
								longComp = (Long.parseLong(strBefore) - Long.parseLong(strVData.substring(11, 25))) / 100;

								if (longComp > 0)
								{
									if (longBefore < longComp)
									{
										longBefore = longComp;

										this.writeLog("시간오차 : " + longComp + "|" + strBefore + "|" + strVData.substring(11, 25));
									}
								}
								else if (longComp == 0)
								{
									longBefore = 0;
								}
								else
								{
									if (longBefore < -1)
									{
										this.writeLog("시간공백 : " + strBefore + "|" + strVData.substring(11, 25));
									}

									strBefore = strVData.substring(11, 25);
								}
							}
							else
							{
								strBefore = strVData.substring(11, 25);
							}

							intPos += 68;

						}
						catch (Exception e)
						{
							this.writeLog(" 데이터 처리 오류 : " + cur + " | " + e.getMessage());
							this.writeLog(" Position : " + intPos + " | 길이 : " + arrByte.length);

							break;
						}

					}

					cur++;
				}

			}
			
			
			try
			{
				this.writeLog("End Date : " + new SimpleDateFormat("yyMMddHHmmssSS").parse(tdv.getStrDriveTime()) + " / Speed Value : " + tdv.getStrSpeed() + " / Rpm Value : " + tdv.getStrRpm());
			}
			catch (ParseException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			br.close();

			this.writeLog(">>>>>>>>>>>>>>> File Verification End <<<<<<<<<<<<<<<<");
		}

		private String strBefore = "00000000";
		private long longComp = 0;
		private long longBefore = 0;

		private TripDataVO tdbv = null;

		/**
		 * 운행데이터 항목 검사
		 * 
		 * @param tdv
		 * @throws CloneNotSupportedException
		 */
		private void verifyDriveData(TripDataVO tdv) throws CloneNotSupportedException
		{
			if (tdbv != null)
			{
				// this.writeLog("tdbv:" + tdbv.getStrDriveTime());
				// this.writeLog("tdv:" + tdv.getStrDriveTime());

				// 일일 주행 거리 검증 //
				this.verifyDayDist(tdbv.getStrDayDist(), tdv.getStrDayDist());

				// 누적 주행 거리 검증 //
				this.verifyAccuDist(tdbv.getStrAccuDist(), tdv.getStrAccuDist());

				// 운행일시 검증 //
				this.verifyDriveTime(tdbv.getStrDriveTime(), tdv.getStrDriveTime());

				// 속도 검증 //
				this.verifySpeed(tdv.getStrSpeed());

				// RPM 검증 //
				this.verifyRpm(tdv.getStrRpm());

				// Break 신호 검증 //
				this.verfiyBreak(tdv.getStrBreak());

				// GPS X 검증 //
				this.verifyXcrd(tdv.getStrXcrd(), tdv.getStrStatus());

				// GPS Y 검증 //
				this.verifyYcrd(tdv.getStrYcrd(), tdv.getStrStatus());

				// 방위각 검증 //
				this.verifyRd(tdv.getStrRd());

				// 가속도 X 검증 //
				this.verifyAccelX(tdv.getStrAccelVx());

				// 가속도 Y 검증 //
				this.verifyAccelY(tdv.getStrAccelVy());

				// 기기상태 검증 //
				this.verifyStatus(tdv.getStrStatus());
			}

			tdbv = (TripDataVO) CloneUtils.clone(tdv);

		}

		/**
		 * 기기 상태 검증
		 * 
		 * @param strCurStatus
		 */
		private void verifyStatus(String strCurStatus)
		{
			int curStatus = 0;

			try
			{
				curStatus = Integer.parseInt(strCurStatus);
			}
			catch (NumberFormatException e)
			{
				this.writeLog(" 기기상태검증오류값 : " + strCurStatus);
			}

			switch (curStatus)
			{
			case 0:
			case 11:
			case 12:
			case 13:
			case 14:
			case 21:
			case 22:
			case 31:
			case 32:
			case 41:
			case 99:

				break;

			default:

				curStatus = -1;

				break;
			}

			if (curStatus < 0)
			{
				this.writeLog(" 기기상태검증오류값 : " + strCurStatus);
			}
		}

		/**
		 * 가속도 Y 검증
		 * 
		 * @param strCurAccelY
		 */
		private void verifyAccelY(String strCurAccelY)
		{
			float curAccelY = 0;

			try
			{
				curAccelY = Float.parseFloat(strCurAccelY);
			}
			catch (NumberFormatException e)
			{
				this.writeLog(" 가속도Y오류값 : " + strCurAccelY);
			}

			// this.writeLog("" + curAccelY);

			if (curAccelY < -100 || curAccelY > 100)
			{
				this.writeLog(" 가속도Y오류값 : " + strCurAccelY);
			}
		}

		/**
		 * 가속도 X 검증
		 * 
		 * @param strCurAccelX
		 */
		private void verifyAccelX(String strCurAccelX)
		{
			float curAccelX = 0;

			try
			{
				curAccelX = Float.parseFloat(strCurAccelX);
			}
			catch (NumberFormatException e)
			{
				this.writeLog(" 가속도X오류값 : " + strCurAccelX);
			}

			if (curAccelX < -100 || curAccelX > 100)
			{
				this.writeLog(" 가속도X오류값 : " + strCurAccelX);
			}
		}

		/**
		 * 방위각 검증
		 * 
		 * @param strCurRd
		 */
		private void verifyRd(String strCurRd)
		{
			int curRd = 0;

			try
			{
				curRd = Integer.parseInt(strCurRd);
			}
			catch (NumberFormatException e)
			{
				this.writeLog(" 방위각오류값 : " + strCurRd);
			}

			if (curRd < 0 || curRd > 360)
			{
				this.writeLog(" 방위각오류값 : " + strCurRd);
			}
		}

		/**
		 * GPS Y좌표 검증
		 * 
		 * @param strCurYcrd
		 * @param strStatus
		 */
		private void verifyYcrd(String strCurYcrd, String strStatus)
		{
			int curYcrd = 0;

			try
			{
				curYcrd = Integer.parseInt(strCurYcrd);
			}
			catch (NumberFormatException e)
			{
				this.writeLog(" Ycrd오류값 : " + strCurYcrd);
			}

			// this.writeLog(strCurXcrd); // 036945092

			if (!strStatus.equals("11")) // 기기 상태에 GPS 오류가 아닐 경우만 검증 한다. //
			{
				if (curYcrd < 32000000 || curYcrd > 39000000)
				{
					this.writeLog(" Ycrd오류값 : " + strCurYcrd);
				}
			}
		}

		/**
		 * GPS X좌표 검증
		 * 
		 * @param strCurXcrd
		 * @param strStatus
		 */
		private void verifyXcrd(String strCurXcrd, String strStatus)
		{
			int curXcrd = 0;

			try
			{
				curXcrd = Integer.parseInt(strCurXcrd);
			}
			catch (NumberFormatException e)
			{
				this.writeLog(" Xcrd오류값 : " + strCurXcrd);
			}

			// this.writeLog(strCurXcrd); // 127848352

			if (!strStatus.equals("11")) // 기기 상태에 GPS 오류가 아닐 경우만 검증 한다. //
			{
				if (curXcrd < 124000000 || curXcrd > 132000000)
				{
					this.writeLog(" Xcrd오류값 : " + strCurXcrd);
				}
			}

		}

		/**
		 * Break 신호 검증
		 * 
		 * @param strCurBreak
		 */
		private void verfiyBreak(String strCurBreak)
		{
			if (!strCurBreak.equals("0") && !strCurBreak.equals("1"))
			{
				this.writeLog(" Break오류값 : " + strCurBreak);
			}
		}

		/**
		 * RPM 검증
		 * 
		 * @param strCurRpm
		 */
		private void verifyRpm(String strCurRpm)
		{
			int curRpm = 0;

			try
			{
				curRpm = Integer.parseInt(strCurRpm);
			}
			catch (NumberFormatException e)
			{
				this.writeLog(" RPM오류값 : " + strCurRpm);
			}

			if (curRpm < 0)
			{
				this.writeLog(" RPM오류값 : " + strCurRpm);
			}
		}

		/**
		 * Speed 검증
		 * 
		 * @param strCurSpeed
		 */
		private void verifySpeed(String strCurSpeed)
		{
			int curSpeed = 0;

			try
			{
				curSpeed = Integer.parseInt(strCurSpeed);
			}
			catch (NumberFormatException e)
			{
				this.writeLog(" [NumberFormatException] 속도오류값 : " + strCurSpeed);
			}

			if (curSpeed < 0 || curSpeed > 180)
			{
				this.writeLog(" 속도오류값 : " + strCurSpeed);
			}
		}

		/**
		 * 운행시가 검증
		 * 
		 * @param strBeforeDriveTime
		 * @param strCurDriveTime
		 */
		private void verifyDriveTime(String strBeforeDriveTime, String strCurDriveTime)
		{
			// this.writeLog(strBeforeDriveTime); // 19093000115800
			Date beforeDate = null;
			Date curDate = null;

			try
			{
				beforeDate = new SimpleDateFormat("yyMMddHHmmssSS").parse(strBeforeDriveTime);
			}
			catch (ParseException e)
			{
				this.writeLog(" 운행일시오류값 : " + strBeforeDriveTime);
			}

			try
			{
				curDate = new SimpleDateFormat("yyMMddHHmmssSS").parse(strCurDriveTime);
			}
			catch (ParseException e)
			{
				this.writeLog(" 운행일시오류값 : " + strCurDriveTime);
			}

			// this.writeLog(" 운행일시오류 값 : " + beforeDate.compareTo(curDate));

			if (beforeDate.compareTo(curDate) > 0)
			{
				this.writeLog(" 이전운행일시비교시작 : " + strBeforeDriveTime + " ~ " + strCurDriveTime);
				strBeforMaxDriveTime = strBeforeDriveTime;
			}
			else
			{
				Date beforeMaxDate = null;

				if (!strBeforMaxDriveTime.equals(""))
				{
					try
					{
						beforeMaxDate = new SimpleDateFormat("yyMMddHHmmssSS").parse(strBeforMaxDriveTime);
					}
					catch (ParseException e)
					{
						// TODO Auto-generated catch block
						this.writeLog(" 이전운행MAX일시변환오류값 : " + strBeforMaxDriveTime);
					}

					if (beforeMaxDate.compareTo(curDate) >= 0)
					{
						this.writeLog(" 이전운행일시비교값 : " + strBeforMaxDriveTime + " ~ " + strCurDriveTime);
					}
				}
			}
		}

		private String strBeforMaxDriveTime = "";

		/**
		 * 누적 주행 거리 검증
		 * 
		 * @param strBeforeAccuDist
		 * @param strCurAccuDist
		 */
		private void verifyAccuDist(String strBeforeAccuDist, String strCurAccuDist)
		{
			int beforeAccuDist = 0;
			int curAccuDist = 0;

			try
			{
				beforeAccuDist = Integer.parseInt(strBeforeAccuDist);
			}
			catch (NumberFormatException e)
			{
				this.writeLog(" 누적주행거리오류값 : " + strBeforeAccuDist);
			}

			try
			{
				curAccuDist = Integer.parseInt(strCurAccuDist);
			}
			catch (NumberFormatException e)
			{
				this.writeLog(" 누적주행거리오류값 : " + strCurAccuDist);
			}

			// this.writeLog(" 누적주행거리오류값 : " + beforeAccuDist + " > " + curAccuDist);

			if (curAccuDist < 0)
			{
				this.writeLog(" 누적주행거리오류값 : " + beforeAccuDist + " > " + curAccuDist);
			}

			if (beforeAccuDist > curAccuDist)
			{
				this.writeLog(" 누적주행거리오류값 : " + beforeAccuDist + " > " + curAccuDist);
			}
		}

		/**
		 * 일일 주행 거리 검증
		 * 
		 * @param strBeforeDayDist
		 * @param strCurDayDist
		 */
		private void verifyDayDist(String strBeforeDayDist, String strCurDayDist)
		{
			int beforeDayDist = 0;
			int curDayDist = 0;

			try
			{
				beforeDayDist = Integer.parseInt(strBeforeDayDist);
			}
			catch (NumberFormatException e)
			{
				this.writeLog(" 일일주행거리오류값 : " + strBeforeDayDist);
			}

			try
			{
				curDayDist = Integer.parseInt(strCurDayDist);
			}
			catch (NumberFormatException e)
			{
				this.writeLog(" 일일주행거리오류값 : " + strCurDayDist);
			}

			// this.writeLog(" 일일주행거리오류값 : " + beforeDayDist + " > " + curDayDist);

			if (curDayDist < 0)
			{
				this.writeLog(" 일일주행거리오류값 : " + beforeDayDist + " > " + curDayDist);
			}

			if (beforeDayDist > curDayDist)
			{
				this.writeLog(" 일일주행거리오류값 : " + beforeDayDist + " > " + curDayDist);
			}
		}

		/**
		 * Log 기록
		 * 
		 * @param strLog
		 */
		private void writeLog(String strLog)
		{
			System.out.println("[" + strHndset + "][" + strCarNo + "][" + strFileName + "]" + strLog);
		}
	}

}
