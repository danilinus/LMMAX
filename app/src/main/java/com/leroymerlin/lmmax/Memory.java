package com.leroymerlin.lmmax;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.drive.DriveClient;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveResourceClient;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public final class Memory {
	public static String otdel;
	public static String city;

	public static String[] g;

	public static GoogleSignInClient mGoogleSignInClient = null;
	public static DriveClient mDriveClient = null;
	public static DriveResourceClient mDriveResourceClient = null;
	public static DriveContents tovarData = null;
	public static String Address = "";
	public static MetadataBuffer MB;

	public static String newFile = "";

	public static String formats = ".apk .jpg";

	public static String inf = "";

	public static String pass_settings = "Passwd81";

	public static boolean findLm = true;

	public static void createFile(String Title, String Text) {
		final Task<DriveFolder> rootFolderTask = mDriveResourceClient.getRootFolder();
		final Task<DriveContents> createContentsTask = mDriveResourceClient.createContents();
		Tasks.whenAll(rootFolderTask, createContentsTask).continueWithTask(task -> {
			DriveFolder parentFolder = rootFolderTask.getResult();
			DriveContents contents = createContentsTask.getResult();
			OutputStream outputStream = contents.getOutputStream();
			try (Writer writer = new OutputStreamWriter(outputStream)) {
				writer.write(Text);
			}
			MetadataChangeSet changeSet = new MetadataChangeSet.Builder().setTitle(Title).setMimeType("text/plain").setStarred(true).build();
			return mDriveResourceClient.createFile(parentFolder, changeSet, contents);
		});
	}

	public static void createFile(String Title, InputStream inputStream) {
		final Task<DriveFolder> rootFolderTask = mDriveResourceClient.getRootFolder();
		final Task<DriveContents> createContentsTask = mDriveResourceClient.createContents();
		Tasks.whenAll(rootFolderTask, createContentsTask).continueWithTask(task -> {
			DriveFolder parentFolder = rootFolderTask.getResult();
			DriveContents contents = createContentsTask.getResult();
			OutputStream outputStream = contents.getOutputStream();
			IOUtils.copy(inputStream, outputStream);
			MetadataChangeSet changeSet = new MetadataChangeSet.Builder().setTitle(Title).setStarred(true).build();
			return mDriveResourceClient.createFile(parentFolder, changeSet, contents);
		});
	}

	public static void createFile(String Title, OutputStream putStream) {
		final Task<DriveFolder> rootFolderTask = mDriveResourceClient.getRootFolder();
		final Task<DriveContents> createContentsTask = mDriveResourceClient.createContents();
		Tasks.whenAll(rootFolderTask, createContentsTask).continueWithTask(task -> {
			DriveFolder parentFolder = rootFolderTask.getResult();
			DriveContents contents = createContentsTask.getResult();
			OutputStream outputStream = contents.getOutputStream();
			outputStream.write(((ByteArrayOutputStream) putStream).toByteArray());
			MetadataChangeSet changeSet = new MetadataChangeSet.Builder().setTitle(Title).setStarred(true).build();
			return mDriveResourceClient.createFile(parentFolder, changeSet, contents);
		});
	}

	public static void createFile(String Title) {
		mDriveResourceClient.getRootFolder().continueWithTask(task -> {
			DriveFolder parentFolder = task.getResult();
			MetadataChangeSet changeSet = new MetadataChangeSet.Builder().setTitle(Title).setMimeType("text/plain").setStarred(true).build();
			return mDriveResourceClient.createFile(parentFolder, changeSet, null);
		});
	}

	public static void deleteFile(String Title) {
		getMetadataBuffer(Title).continueWithTask(task -> mDriveResourceClient.delete(task.getResult().get(0).getDriveId().asDriveFile()));
	}

	public static void deleteFile(String Title, int o) {
		getMetadataBuffer(Title).continueWithTask(task -> mDriveResourceClient.delete(task.getResult().get(o).getDriveId().asDriveFile()));
	}

	public static void deleteFile(DriveContents dc) {
		mDriveResourceClient.delete(dc.getDriveId().asDriveFile());
	}

	public static Task<MetadataBuffer> getMetadataBuffer(String Title) {
		return mDriveResourceClient.query(new Query.Builder().addFilter(Filters.eq(SearchableField.TITLE, Title)).build());
	}

	public static Task<DriveContents> openReadFile(Task<MetadataBuffer> queryTask) {
		return queryTask.continueWithTask(task -> {
			return mDriveResourceClient.openFile(task.getResult().get(0).getDriveId().asDriveFile(), DriveFile.MODE_READ_ONLY);
		});
	}

	public static Task<DriveContents> openReadFile(MetadataBuffer queryTask) {
		return mDriveResourceClient.openFile(queryTask.get(0).getDriveId().asDriveFile(), DriveFile.MODE_READ_ONLY);
	}

	public static Task<DriveContents> openReadFile(String Title, int n) {
		return Memory.getMetadataBuffer(Title).continueWithTask(task -> {
			return mDriveResourceClient.openFile(task.getResult().get(n).getDriveId().asDriveFile(), DriveFile.MODE_READ_ONLY);
		});

	}

	public static Task<DriveContents> openReadFile(MetadataBuffer queryTask, int n) {
		return mDriveResourceClient.openFile(queryTask.get(n).getDriveId().asDriveFile(), DriveFile.MODE_READ_ONLY);
	}

	public static Task<DriveContents> openReadFile(DriveFile queryTask) {
		return mDriveResourceClient.openFile(queryTask, DriveFile.MODE_READ_ONLY);
	}

	public static Task<DriveContents> openReadFile(String Title) {
		return getMetadataBuffer(Title).continueWithTask(task -> {
			return mDriveResourceClient.openFile(task.getResult().get(0).getDriveId().asDriveFile(), DriveFile.MODE_READ_ONLY);
		});
	}


	public static Task<DriveContents> openWriteFile(Task<MetadataBuffer> queryTask) {
		return queryTask.continueWithTask(task -> {
			return mDriveResourceClient.openFile(task.getResult().get(0).getDriveId().asDriveFile(), DriveFile.MODE_WRITE_ONLY);
		});
	}

	public static Task<DriveContents> openWriteFile(MetadataBuffer queryTask) {
		return mDriveResourceClient.openFile(queryTask.get(0).getDriveId().asDriveFile(), DriveFile.MODE_WRITE_ONLY);
	}

	public static Task<DriveContents> openWriteFile(MetadataBuffer queryTask, int n) {
		return mDriveResourceClient.openFile(queryTask.get(n).getDriveId().asDriveFile(), DriveFile.MODE_WRITE_ONLY);
	}

	public static Task<DriveContents> openWriteFile(DriveFile queryTask) {
		return mDriveResourceClient.openFile(queryTask, DriveFile.MODE_WRITE_ONLY);
	}

	public static Task<DriveContents> openWriteFile(String Title) {
		return getMetadataBuffer(Title).continueWithTask(task -> {
			return mDriveResourceClient.openFile(task.getResult().get(0).getDriveId().asDriveFile(), DriveFile.MODE_WRITE_ONLY);
		});
	}


	public static Task<DriveContents> openReadWriteFile(Task<MetadataBuffer> queryTask) {
		return queryTask.continueWithTask(task -> {
			return mDriveResourceClient.openFile(task.getResult().get(0).getDriveId().asDriveFile(), DriveFile.MODE_READ_WRITE);
		});
	}

	public static Task<DriveContents> openReadWriteFile(MetadataBuffer queryTask) {
		return mDriveResourceClient.openFile(queryTask.get(0).getDriveId().asDriveFile(), DriveFile.MODE_READ_WRITE);
	}

	public static Task<DriveContents> openReadWriteFile(MetadataBuffer queryTask, int n) {
		return mDriveResourceClient.openFile(queryTask.get(n).getDriveId().asDriveFile(), DriveFile.MODE_READ_WRITE);
	}

	public static Task<DriveContents> openReadWriteFile(DriveFile queryTask) {
		return mDriveResourceClient.openFile(queryTask, DriveFile.MODE_READ_WRITE);
	}

	public static Task<DriveContents> openReadWriteFile(String Title) {
		return getMetadataBuffer(Title).continueWithTask(task -> {
			return mDriveResourceClient.openFile(task.getResult().get(0).getDriveId().asDriveFile(), DriveFile.MODE_READ_WRITE);
		});
	}

	public static Task<String> getTextFromFile(String Title) {
		return openReadFile(Title).continueWith(e ->
		{
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(e.getResult().getInputStream()))) {
				StringBuilder builder = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) builder.append(line).append("\n");
				return builder.toString();
			}
		});
	}

	public static Task<String> getTextFromFile(String Title, int n) {
		return openReadFile(Title, n).continueWith(e ->
		{
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(e.getResult().getInputStream()))) {
				StringBuilder builder = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) builder.append(line).append("\n");
				return builder.toString();
			}
		});
	}

	public static Task<String> getTextFromFile(MetadataBuffer content) {
		return openReadFile(content).continueWith(e ->
		{
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(e.getResult().getInputStream()))) {
				StringBuilder builder = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) builder.append(line).append("\n");
				return builder.toString();
			}
		});
	}

	public static Task<String> getTextFromFile(MetadataBuffer content, int n) {
		return openReadFile(content, n).continueWith(e ->
		{
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(e.getResult().getInputStream()))) {
				StringBuilder builder = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) builder.append(line).append("\n");
				return builder.toString();
			}
		});
	}

	public static Task<String> getTextFromFile(Task<DriveContents> content) {
		return content.continueWith(e ->
		{
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(e.getResult().getInputStream()))) {
				StringBuilder builder = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) builder.append(line).append("\n");
				return builder.toString();
			}
		});
	}

	public static Task<String> getTextFromFile(DriveFile file) {
		return openReadFile(file).continueWith(e ->
		{
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(e.getResult().getInputStream()))) {
				StringBuilder builder = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) builder.append(line).append("\n");
				return builder.toString();
			}
		});
	}

	public static Task<Bitmap> getImageFromFile(String Title) {
		return openReadFile(Title).continueWith(task -> {
			return BitmapFactory.decodeStream(task.getResult().getInputStream());
		});
	}


	public static void replaceFile(String Title, String newContent) {
		Memory.getMetadataBuffer(Title).continueWith(task2 ->
		{
			if (task2.getResult().getCount() > 0)
				openWriteFile(Title).continueWithTask(task -> {
					DriveContents driveContents = task.getResult();
					try (OutputStream out = driveContents.getOutputStream()) {
						out.write(newContent.getBytes());
					}
					return mDriveResourceClient.commitContents(driveContents, new MetadataChangeSet.Builder().setStarred(true).setLastViewedByMeDate(new Date()).build());
				});
			else
				Memory.createFile(Title, newContent);
			return null;
		});
	}

	public static void replaceFile(String Title, OutputStream outputStream) {
		Memory.getMetadataBuffer(Title).continueWith(task2 ->
		{
			if (task2.getResult().getCount() > 0)
				openWriteFile(Title).continueWithTask(task -> {
					DriveContents driveContents = task.getResult();
					try (OutputStream out = driveContents.getOutputStream()) {
						out.write(((ByteArrayOutputStream) outputStream).toByteArray());
					}
					return mDriveResourceClient.commitContents(driveContents, new MetadataChangeSet.Builder().setStarred(true).setLastViewedByMeDate(new Date()).build());
				});
			else
				Memory.createFile(Title, outputStream);
			return null;
		});
	}

	public static void replaceFile(String Title, InputStream inputStream) {
		Memory.getMetadataBuffer(Title).continueWith(task2 ->
		{
			if (task2.getResult().getCount() > 0)
				openWriteFile(Title).continueWithTask(task -> {
					DriveContents driveContents = task.getResult();
					try (OutputStream out = driveContents.getOutputStream()) {
						IOUtils.copy(inputStream, out);
					}
					return mDriveResourceClient.commitContents(driveContents, new MetadataChangeSet.Builder().setStarred(true).setLastViewedByMeDate(new Date()).build());
				});
			else
				Memory.createFile(Title, inputStream);
			return null;
		});
	}

	public static void replaceFile(MetadataBuffer mb, String newContent) {
		openWriteFile(mb).continueWithTask(task -> {
			DriveContents driveContents = task.getResult();
			try (OutputStream out = driveContents.getOutputStream()) {
				out.write(newContent.getBytes());
			}
			return mDriveResourceClient.commitContents(driveContents, new MetadataChangeSet.Builder().setStarred(true).setLastViewedByMeDate(new Date()).build());
		});
	}

	public static void replaceFile(DriveContents dc, String newContent) {
		openWriteFile(dc.getDriveId().asDriveFile()).continueWithTask(task -> {
			DriveContents driveContents = task.getResult();
			try (OutputStream out = driveContents.getOutputStream()) {
				out.write(newContent.getBytes());
			}
			return mDriveResourceClient.commitContents(driveContents, new MetadataChangeSet.Builder().setStarred(true).setLastViewedByMeDate(new Date()).build());
		});
	}

	public static void replaceFile(MetadataBuffer mb, InputStream inputStream) {
		openWriteFile(mb).continueWithTask(task -> {
			DriveContents driveContents = task.getResult();
			try (OutputStream out = driveContents.getOutputStream()) {
				IOUtils.copy(inputStream, out);
			}
			return mDriveResourceClient.commitContents(driveContents, new MetadataChangeSet.Builder().setStarred(true).setLastViewedByMeDate(new Date()).build());
		});
	}

	public static void replaceFile(MetadataBuffer mb, String newContent, int n) {
		openWriteFile(mb, n).continueWithTask(task -> {
			DriveContents driveContents = task.getResult();
			try (OutputStream out = driveContents.getOutputStream()) {
				out.write(newContent.getBytes());
			}
			return mDriveResourceClient.commitContents(driveContents, new MetadataChangeSet.Builder().setStarred(true).setLastViewedByMeDate(new Date()).build());
		});
	}

	public static void replaceFile(MetadataBuffer mb, InputStream inputStream, int n) {
		openWriteFile(mb, n).continueWithTask(task -> {
			DriveContents driveContents = task.getResult();
			try (OutputStream out = driveContents.getOutputStream()) {
				IOUtils.copy(inputStream, out);
			}
			return mDriveResourceClient.commitContents(driveContents, new MetadataChangeSet.Builder().setStarred(true).setLastViewedByMeDate(new Date()).build());
		});
	}

	public static String ChinT(String s, String cont) {
		String itog = "";
		String[] ss = cont.split("\n");
		for (int i = 0; i < ss.length; i++)
			if (!ss[i].equals(s))
				itog += ss[i] + "\n";
		return itog;
	}

	public static void showMessage(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	public static String removeDoubleSpace(String s) {
		while (s.charAt(0) == ' ') s = s.substring(1);
		while (s.charAt(s.length() - 1) == ' ') s = s.substring(0, s.length() - 2);
		int lng = s.length();
		for (int i = 0; i < lng - 2; i++)
			if (s.charAt(i) == ' ' && s.charAt(i + 1) == ' ') {
				s = s.substring(0, i) + s.substring(i + 1);
				--i;
				lng--;
			}
		return s;
	}

	//Сортирует и удалет одинаковые адреса
	public static String sortAddress(String s) {
		List<String> str = new ArrayList<>(Arrays.asList(s.split(" ")));
		Collections.sort(str);
		s = "";
		for (int i = 0; i < str.size() - 1; i++)
			if (!str.get(i).equals(str.get(i + 1)))
				s += str.get(i) + " ";
		s += str.get(str.size() - 1);
		return s;
	}

	//*******************************NEW TECHNOLOGY*******************************

	//Данные базы excel
	public static BaseOtdel dataBaseOtdel = new BaseOtdel();

	public static BaseAddress tovarsBaseOtdel = new BaseAddress();


	public static BaseOtdel ExcelToBase(InputStream inputStream) {
		BaseOtdel baseOtdel = new BaseOtdel();
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			XSSFSheet sheet = workbook.getSheetAt(0);
			int rowsCount = sheet.getPhysicalNumberOfRows();
			baseOtdel = new BaseOtdel();
			FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
			for (int r = 1; r < rowsCount; r++) {
				Row row = sheet.getRow(r);
				String lmcode = getCellAsString(row, 2, formulaEvaluator).replace(".", "").replace("E7", "");
				while (8 - lmcode.length() > 0) lmcode += '0';
				baseOtdel.add(new BaseTovar(getCellAsString(row, 4, formulaEvaluator), lmcode, getCellAsString(row, 3, formulaEvaluator)));
			}
		} catch (Exception ex) {
			Log.e("LM MAX ExcelToBase", ex.getMessage());
		}
		return baseOtdel;
	}

	//Получение данных клетки в Excel
	private static String getCellAsString(Row row, int c, FormulaEvaluator formulaEvaluator) {
		String value = "";
		try {
			Cell cell = row.getCell(c);
			CellValue cellValue = formulaEvaluator.evaluate(cell);
			switch (cellValue.getCellType()) {
				case Cell.CELL_TYPE_BOOLEAN:
					value = "" + cellValue.getBooleanValue();
					break;
				case Cell.CELL_TYPE_NUMERIC:
					double numericValue = cellValue.getNumberValue();
					if (HSSFDateUtil.isCellDateFormatted(cell)) {
						double date = cellValue.getNumberValue();
						SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy");
						value = formatter.format(HSSFDateUtil.getJavaDate(date));
					} else {
						value = Double.toString(numericValue);
					}
					break;
				case Cell.CELL_TYPE_STRING:
					value = "" + cellValue.getStringValue();
					break;
				default:
			}
		} catch (NullPointerException e) {
			Log.e("LM MAX getCellAsString", e.getMessage());
		}
		return value;
	}

	//Создаем класс на диске
	public static void createFile(String Title, Object object) {
		final Task<DriveFolder> rootFolderTask = mDriveResourceClient.getRootFolder();
		final Task<DriveContents> createContentsTask = mDriveResourceClient.createContents();
		Tasks.whenAll(rootFolderTask, createContentsTask).continueWithTask(task -> {
			DriveFolder parentFolder = rootFolderTask.getResult();
			DriveContents contents = createContentsTask.getResult();
			ObjectOutputStream os = new ObjectOutputStream(contents.getOutputStream());
			os.writeObject(object);
			os.close();
			MetadataChangeSet changeSet = new MetadataChangeSet.Builder().setTitle(Title).setStarred(true).build();
			return mDriveResourceClient.createFile(parentFolder, changeSet, contents);
		});
	}

	//Замена класса на диске
	public static Task<Object> replaceFile(String Title, Object object) {
		return Memory.getMetadataBuffer(Title).continueWith(task2 ->
		{
			if (task2.getResult().getCount() > 0)
				openWriteFile(Title).continueWithTask(task -> {
					DriveContents driveContents = task.getResult();
					ObjectOutputStream os = new ObjectOutputStream(driveContents.getOutputStream());
					os.writeObject(object);
					os.close();
					return mDriveResourceClient.commitContents(driveContents, new MetadataChangeSet.Builder().setStarred(true).setLastViewedByMeDate(new Date()).build());
				});
			else
				Memory.createFile(Title, object);
			return null;
		});
	}

	//Функция преобразования потока байтов в объект
	public static Object ConvertToObject(InputStream inputStream) {
		Object object = null;
		try {
			ObjectInputStream is = new ObjectInputStream(inputStream);
			object = is.readObject();
			is.close();
		} catch (Exception ex) {
			Log.e("LM MAX LoadObject", ex.getMessage());
		}
		return object;
	}

	//Обновление данных товаров
	public static Task<Object> UpdateTovarBase() {
		return Memory.getMetadataBuffer("KLK" + Memory.otdel).continueWithTask(task ->
		{
			if (task.getResult().getCount() > 0)
				return Memory.openReadFile("KLK" + Memory.otdel).continueWith(task1 -> {
					Memory.tovarsBaseOtdel = (BaseAddress) Memory.ConvertToObject(task1.getResult().getInputStream());
					return Memory.tovarsBaseOtdel;
				});
			else
				Memory.createFile("KLK" + Memory.otdel);
			return null;
		});
	}

	//Обновление данных товаров на сервере
	public static Task<Object> LoadToCloudTovarBase() {
		return replaceFile("KLK" + Memory.otdel, Memory.tovarsBaseOtdel);
	}
}