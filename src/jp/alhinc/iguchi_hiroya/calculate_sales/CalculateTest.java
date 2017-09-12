package jp.alhinc.iguchi_hiroya.calculate_sales;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class CalculateTest {
      public static void main(String[] args){
    	 //1.支店定義ファイル読み込み
    	//コードと支店のHashMapを作成
 		  HashMap<String,String> branchNameMap = new HashMap<String, String>();
 		 Map<String,Long> branchSaleMap = new HashMap<String,Long>();
 		 HashMap<String,String> commodityNameMap = new HashMap<String, String>();
 		 Map<String,Long> commoditySaleMap = new HashMap<String, Long>();

 		if(args.length != 1){
			  System.out.println("予期せぬエラーが発生しました");
			  return;
		  }


		  BufferedReader br = null;

    	  try{
    		  //指定ディレクトリからファイルを開く
    		  File file1 = new File(args[0]+File.separator +"branch.lst");
    		  //支店定義ファイルの存在判別
    		  if(!file1.exists()){
    			  System.out.println("支店定義ファイルが存在しません");
    			  return;
    		  }
    		  //1行ずつデータを読み込む
    		  FileReader fr1 = new FileReader(file1);
    		  br = new BufferedReader(fr1);
    		  String s1 ;



    		  //読み込んだ行がnullじゃない限り繰り返し
    		  while((s1 = br.readLine()) != null ){
    			  String[] branchFile = s1.split(",");//s1を","で分割したものを格納する配列を作成
    			  //formatの確認
    			  if(!branchFile[0].matches("^[0-9]*$")||branchFile[0].length() != 3 || branchFile.length != 2){
    				  System.out.println("支店定義ファイルのフォーマットが不正です");
    				  return;
    			  }
    			  branchNameMap.put(branchFile[0], branchFile[1]);
    			  branchSaleMap.put(branchFile[0], (long)0);
    		  }

    	  }catch(IOException e){
    		  System.out.println("予期せぬエラーが発生しました");
    		  return;
    	  }finally{

              if (br != null)
                  try {
                      br.close();
                  }catch(IOException e){
            		  System.out.println("予期せぬエラーが発生しました");
            		  return;
            	  }
          }

    	  //2.商品定義ファイル読み込み
    	  BufferedReader br1 = null;
    	  try{
    		  //指定ディレクトリからファイルを開く
    		  File file2 = new File(args[0]+File.separator +"commodity.lst");
    		  //支店定義ファイルの存在判別
    		  if(args.length != 1){
    			  System.out.println("予期せぬエラーが発生しました");
    			  return;
    		  }
    		  else if(!file2.exists()){
    			  System.out.println("商品定義ファイルが存在しません");
    			  return;
    		  }
    		  //1行ずつデータを読み込む
    		  FileReader fr2 = new FileReader(file2);
    		  br1 = new BufferedReader(fr2);
    		  String s2;
    		  //商品コードと商品名のHashMapを作成


    		  //読み込んだ行がnullじゃない限り繰り返し
    		  while((s2  = br1.readLine())!= null ){
    			  String[] commodityFile = s2.split(",");//s2を","で分割したものを配列へ。
    			  //ファイルフォーマット判別
    			  if(!commodityFile[0].matches("^[0-9a-zA-Z]*$")||commodityFile[0].length() != 8 || commodityFile.length != 2){
    				  System.out.println("商品定義ファイルのフォーマットが不正です");
    				  return;
    			  }
    			  commodityNameMap.put(commodityFile[0], commodityFile[1]);
    			  commoditySaleMap.put(commodityFile[0], (long)0);

    		  }

    	  }catch(IOException e1){
    		  System.out.println("予期せぬエラーが発生しました");
    		  return;
    	  }finally{

              if (br1 != null)
                  try {
                      br1.close();
                  }catch(IOException e){
            		  System.out.println("予期せぬエラーが発生しました");
            		  return;
            	  }
          }
    	  //3.集計

          //3-1 連番ファイルの検索
    	  FilenameFilter filter = new FilenameFilter() {

  			public boolean accept(File file, String str){

  				// 拡張子を指定する
  				if (str.endsWith("rcd")){//ファイルの最後がrcdで終わるかどうか判別
  					return true;
  				}else{
  					return false;
  				}
  			}
  		  };

  		//"rcd"が含まれるフィルタを作成する
  		File[] files = new File(args[0]).listFiles(filter);//rcdを持つものだけをファイル型の配列filesに格納する。

  		//連番処理

  		String fileName;
  		ArrayList<String> fileNames = new ArrayList<String>();//String型のArrayListを作成
  		//filesの個数分くりかえし
  		for(int i=0; i< files.length; i++){//配列filesの要素数より小さい場合の処理↓
  		    fileName = files[i].getName();//File型からString型への変換
  		    //桁数が12、かつ数字で始まりdで終わる場合を検討する
  			if(fileName.length()==12 && fileName.matches("^[0-9].*d$") && files[i].isFile()){
  			fileNames.add(fileName);//アレイリストへの追加
  			}
  		 }

  		ArrayList<Integer> sequenceNumber = new ArrayList<Integer>();

  		String nameSeq;
  		for(int i=0; i<files.length; ++i){

  			nameSeq = fileNames.get(i);//ファイル型配列の１つめの要素をString型へ。

  			String[] nameSeqs = nameSeq.split("\\.");//String型配列に8桁番号と拡張子を分割して格納

  			int j = Integer.parseInt(nameSeqs[0]);//String型8桁番号をint型に変更。
  			sequenceNumber.add(j);//int型のアレイリストに番号を格納していく(00000001,00000002)
  			if(i > 0){
  				if(sequenceNumber.get(i) != sequenceNumber.get(i-1)+1){//該当要素と1つ前の要素の連番を比較
  	  				System.out.println("売上ファイル名が連番になっていません");
  	  				return;
  			    }

  			}

  		}

  		//3-2　売り上げファイルの読み込みと合算
  		BufferedReader br2 = null;
  		try{

  		  //売り上げファイルの読み込み
  		  for(int i = 0; i< fileNames.size(); i++){ //ArrayListの要素数より少ない場合=要素すべてに対して以下を実行

  			  ArrayList<String> saleFiles = new ArrayList<String>();//String型のアレイリストを作成
  			  //指定ディレクトリからファイルを開く
    		  File file3 = new File(args[0]+ File.separator + fileNames.get(i));
      		  FileReader fr3 = new FileReader(file3);
      		  br2 = new BufferedReader(fr3);
      		  String s3 ;//1行目が読み込まれる


      		  //支店コードと合計金額の処理
      		  while((s3 = br2.readLine())!= null){//行の中身がある限り下を繰り返す
      			  saleFiles.add(s3);//ArrayListに要素を追加する



      		  }if(saleFiles.size() != 3){//売上ファイルの行数が3行以外の判別
  				  System.out.println(fileNames.get(i)+"のフォーマットが不正です");
  				  return;
  			  }else if(branchSaleMap.get(saleFiles.get(0)) == null){//支店コードに対応する売上がない場合
    				System.out.println(fileNames.get(i)+"の支店コードが不正です");

    			} else if(commoditySaleMap.get(saleFiles.get(1)) == null){//商品コードに対する売上がない場合
      				System.out.println(fileNames.get(i)+"の商品コードが不正です");

    			} else if(!saleFiles.get(2).matches("^[0-9]*$") ){//金額に数値以外が混ざっていればエラー検出
    				  System.out.println("予期せぬエラーが発生しました");
    				  return;
    			  }
      		  else if(branchSaleMap.get(saleFiles.get(0)) != null ){//取り出した要素をmapに入れてみてvalueが存在するか


      			long y = new Long(saleFiles.get(2)).longValue();//String型金額を数値に変換
      			long z = new Long(branchSaleMap.get(saleFiles.get(0))).longValue();//map1aのvalueを数値に変換
      			long t = y + z;
      			String branchSum = String.valueOf(t);

      		   if(branchSum.length() > 10){
      				System.out.println("合計金額が10桁を超えました");
      				return;
      		        }
      		    branchSaleMap.put(saleFiles.get(0), t);
      			}




      		  //商品コードと合計金額の処理
      			long v = new Long(saleFiles.get(2)).longValue();//String型金額を数値に変換
      			long w = new Long(commoditySaleMap.get(saleFiles.get(1))).longValue();//map1aのvalueを数値に変換
      			long x = v + w;
      			String commoditySum = String.valueOf(x);


      			 if(commoditySum.length() > 10){
      				System.out.println("合計金額が10桁を超えました");
      				return;
      			 }
      			commoditySaleMap.put(saleFiles.get(1), x);
      		  }

  		  }catch(IOException e2){

    		 System.out.println("予期せぬエラーが発生しました");
    		  return;


  	      }finally{

              if (br2 != null)
                  try {
                      br2.close();
                  }catch(IOException e){
            		  System.out.println("予期せぬエラーが発生しました");
            		  return;
            	  }
          }
  		//ファイルへの出力
  		//合計金額を降順にする
  		BufferedWriter bw = null;
  		try{
  			List<Map.Entry<String,Long>> entries =
  	              new ArrayList<Map.Entry<String,Long>>(branchSaleMap.entrySet());
  	        Collections.sort(entries, new Comparator<Map.Entry<String,Long>>() {

  	            @Override
  	            public int compare(
  	                  Entry<String,Long> entry1, Entry<String,Long> entry2) {
  	                return ((Long)entry2.getValue()).compareTo((Long)entry1.getValue());
  	            }
  	        });

  	          File file = new File(args[0] + File.separator + "branch.out");
    			FileWriter fw = new FileWriter(file);
    			bw = new BufferedWriter(fw);


    			for (Entry<String,Long> s : entries) {//branchファイルへの出力
    	            bw.write(s.getKey()+","+branchNameMap.get(s.getKey())+","+s.getValue());
    	            bw.newLine();
    	        }

  		}catch(IOException e2){

    		  System.out.println("予期せぬエラーが発生しました");
    		  return;
    	 }finally{

            if (bw != null)
                try {
                    bw.close();
                }catch(IOException e){
          		  System.out.println("予期せぬエラーが発生しました");
          		  return;
          	     }
          }

  	    //商品金額を降順にする
  		    BufferedWriter bw1 = null;
  		try{
			List<Map.Entry<String,Long>> entries1 =
	              new ArrayList<Map.Entry<String,Long>>(commoditySaleMap.entrySet());
	        Collections.sort(entries1, new Comparator<Map.Entry<String,Long>>() {

	            @Override
	            public int compare(
	                  Entry<String,Long> entry1, Entry<String,Long> entry2) {
	                return ((Long)entry2.getValue()).compareTo((Long)entry1.getValue());
	            }
	        });
	        File file1 = new File(args[0] + File.separator + "commodity.out");
 			FileWriter fw1 = new FileWriter(file1);
 			bw1 = new BufferedWriter(fw1);

 			for (Entry<String,Long> s : entries1){//commodityファイルへの出力
	        	bw1.write(s.getKey()+","+commodityNameMap.get(s.getKey())+","+s.getValue());
	        	bw1.newLine();
	        }
  		}catch(IOException e3){
  			System.out.println("予期せぬエラーが発生しました");
  		  return;
	      }finally{

	            if (bw1 != null)
	                try {
	                    bw1.close();
	                }catch(IOException e){
	          		  System.out.println("予期せぬエラーが発生しました");
	          		  return;
	          	  }
	        }

      }//←mainメソッドの終点
      }

