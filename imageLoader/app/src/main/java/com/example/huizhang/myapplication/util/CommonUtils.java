package com.example.huizhang.myapplication.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtils {
    public static String MD5(String origin) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return resultString;
    }
    public static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5","6", "7", "8", "9",
            "a", "b", "c", "d", "e", "f" };


    public final static String getMD5(String s) {

		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void openActivity(Activity activity, Class<?> pClass) {
		openActivity(activity, pClass, null);
	}

	public static void openActivity(Activity activity, Class<?> pClass, Bundle pBundle) {
		Intent intent = new Intent(activity, pClass);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		activity.startActivity(intent);
	}

	public void openActivity(Activity activity, Class<?> cls, String extraName, Object obj) {
		Intent intent = new Intent(activity, cls);
		if (obj != null) {
			intent.putExtra(extraName, (Serializable) obj);
		}
		activity.startActivity(intent);
	}

//	public static void switchContent(FragmentActivity activity, BaseContainerFragment fragment) {
//		// getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,
//		// fragment).commit();
//		if (!activity.isFinishing()) {
//			activity.getSupportFragmentManager().beginTransaction().replace(R.id.realtabcontent, fragment)
//					.commitAllowingStateLoss();
//		}
//	}
//
//	public static void switchContent(FragmentActivity activity, BaseContainerFragment fragment, Bundle b) {
//		if (!activity.isFinishing()) {
//			FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
//			fragment.setArguments(b);
//			transaction.replace(R.id.realtabcontent, fragment);
//			// transaction.commit();
//			transaction.commitAllowingStateLoss();
//		}
//	}

	// 取得版本号
	public static String GetVersion(Context context) {
		try {
			PackageInfo manager = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return manager.versionName;
		} catch (NameNotFoundException e) {
			return "Unknown";
		}
	}

	/**
	 * 检测sdcard是否可用
	 * 
	 * @return true为可用，否则为不可用
	 */
	public static boolean sdCardIsAvailable() {
		String status = Environment.getExternalStorageState();
		if (!status.equals(Environment.MEDIA_MOUNTED))
			return false;
		return true;
	}

	/**
	 * Checks if there is enough Space on SDCard
	 * 
	 * @param updateSize
	 *            Size to Check
	 * @return True if the Update will fit on SDCard, false if not enough space
	 *         on SDCard Will also return false, if the SDCard is not mounted as
	 *         read/write
	 */
	public static boolean enoughSpaceOnSdCard(long updateSize) {
		String status = Environment.getExternalStorageState();
		if (!status.equals(Environment.MEDIA_MOUNTED))
			return false;
		return (updateSize < getRealSizeOnSdcard());
	}

	/**
	 * get the space is left over on sdcard
	 */
	public static long getRealSizeOnSdcard() {
		File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;
	}

	/**
	 * Checks if there is enough Space on phone self
	 * 
	 */
	public static boolean enoughSpaceOnPhone(long updateSize) {
		return getRealSizeOnPhone() > updateSize;
	}

	/**
	 * get the space is left over on phone self
	 */
	public static long getRealSizeOnPhone() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		long realSize = blockSize * availableBlocks;
		return realSize;
	}

	/**
	 * 根据手机分辨率从dp转成px
	 * 
	 * @param context
	 * @param dpValue
	 * @return
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		// return (int) (pxValue / scale + 0.5f) - 15;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 保留小数点1位
	 * 
	 * @param value
	 * @return
	 */
	public static double decimalFormat(double value) {
		DecimalFormat df = new DecimalFormat("#.##");
		return Double.parseDouble(df.format(value));
	}

	/**
	 * 钱分转元
	 * 
	 * @param str
	 * @return
	 */
	public static String getDecimal(String str) {
		double value = Double.parseDouble(str);
		BigDecimal bigDecimal = new BigDecimal(value / 100);
		return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP) + "";
	}

	/**
	 * 日期比较
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int compareDate(String date1, String date2) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date dt1 = df.parse(date1);
			Date dt2 = df.parse(date2);
			if (dt1.before(dt2)) {
				// System.out.println("dt1在dt2前");
				return -1;
			} else if (dt1.after(dt2)) {
				// System.out.println("dt1在dt2后");
				return 1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

	/**
	 * 日期比较
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int compareDateTime(String date1, String date2) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			Date dt1 = df.parse(date1);
			Date dt2 = df.parse(date2);
			if (dt1.before(dt2)) {
				// System.out.println("dt1在dt2前");
				return -1;
			} else if (dt1.after(dt2)) {
				// System.out.println("dt1在dt2后");
				return 1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}

	/**
	 * 计算ImageView的大小（BitmapDrawable）
	 * 
	 * @param resources
	 * @param resourceId
	 * @return
	 */
	public static int[] computeWH(Resources resources, int resourceId) {
		int[] wh = { 0, 0 };
		if (resources == null)
			return wh;

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 1;
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(resources, resourceId, options);
		if (options.mCancel || options.outWidth == -1 || options.outHeight == -1) {
			return wh;
		}

		wh[0] = options.outWidth;
		wh[1] = options.outHeight;

		return wh;
	}

	/**
	 * 把dp转为px
	 * 
	 * @param resources
	 * @param dp
	 * @return
	 */
	public static int parseDpToPx(Resources resources, int dp) {
		int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
		return px;
	}

	/**
	 * 关闭软键盘
	 */
	public static void closeSoftInput(Activity activity) {
		View view = activity.getWindow().peekDecorView();
		if (view != null) {
			InputMethodManager inputmanger = (InputMethodManager) activity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	/**
	 * 获取屏幕参数：宽，高
	 * 
	 * @param context
	 */
	public static DisplayMetrics getScreenParams(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return dm;
	}

	/**
	 * 打开链接
	 * 
	 * @param url
	 * @param context
	 */
	public static void openUrl(String url, Context context) {
		Uri uri = Uri.parse(url);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		context.startActivity(intent);
	}

	/**
	 * 获取版本号(内部识别号)
	 * 
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context) {
		PackageInfo pi = null;
		try {
			pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return pi.versionCode;
	}
}
