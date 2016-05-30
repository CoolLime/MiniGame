package com.coollime.coollime;


import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class CardActivity extends Activity implements View.OnClickListener, Runnable {
	/** Called when the activity is first created. */
	LinearLayout mainLinear; // 전체 레이아웃
	LinearLayout[] linearRows; // 행의 수

	ImageButton[] buttons; // 이미지 버튼 배열
	BitmapDrawable[] bitmapsDrawables; // drawable 배열
	Bitmap[] bitmaps; // 비트맵 배열

	BitmapDrawable bitmapDrawableBack;
	Bitmap bitmapBack;

	int[] indexes; // 카드의 고유번호 저장 배열

	int[] tempArr = null; // 임시 정수 배열	
	int maximum = 4;
	int start, end;

	int[] correctIndexArray;
	int[] selectedIndex;
	int selectedCardIndex;

	Resources res;
	boolean bPassible = true;

	TextView tv;		//게임 진행 상태 텍스트 뷰
	StopWatch sw=null;	//스톱 워치 클래스
	TextView time=null;	//스톱 워치 텍스트뷰
	Button stBtn;

	boolean is_start=false;
	String strTime="000.00";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		int temp = 0;

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card);

		is_start=false;

		tv = (TextView)findViewById( R.id.text );
		mainLinear = (LinearLayout) findViewById(R.id.main_linear); // 전체 리니어
		time = (TextView)findViewById(R.id.time);
		time.setTextColor(Color.RED);
		stBtn=(Button)findViewById(R.id.stBtn);

		sw=new StopWatch();

		tv.setText( R.string.choice1 ); // 메시지가 출력되는 텍스트 뷰
		tv.setTextColor(Color.BLUE);
		tv.setGravity(Gravity.CENTER_HORIZONTAL);

		res = getResources(); // 리소스 얻기
		indexes = new int[ maximum * maximum ]; // 이미지의 고유번호를 기억한다.

		start = 0x7f02008e; // 리소스 번호 처음
		end = 0x7f0200bb; // 리소스 번호 마지막

		// 시작부터 끝까지 카드중 8개의 카드를 뽑아낸다.
		bitmapDrawableBack = (BitmapDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.m00_back,null);
		bitmapBack = bitmapDrawableBack.getBitmap();

		// 랜덤으로 이미지를 읽어온다.
		getRandomImages();
		// 적당히 섞는다.
		mixIndexes();
		// 뷰를 배칠한다
		setViews();
		// 카드 뒷면을 보여준다.
		recoverAllCards();
		// 적절한 초기화
		init();

	}

	private void mixIndexes(){
		int temp;

		// 랜덤으로 중복없이 값을 뽑는다.
		tempArr = getRandomNumbers(0, maximum * maximum - 1, maximum * maximum);
		temp = 0 ;
		//indexes 배열은 랜덤으로 값들이 들어간다
		for( int i = 0 ; i < indexes.length ; i++ ){
			indexes[ tempArr[ i ] ] = temp;
			temp++;

			// 같은 카드가 두 장이 들어가도록 한다.
			if( temp >= maximum * maximum / 2 )
				temp = 0;
		}
	}

	private void getRandomImages(){

		int temp=0;
		bitmapsDrawables = null;

		// 랜덤으로 중복없이 뽑는다. 총 50여장의 카드중 maximum * maximum / 2 개의 카드를 뽑는다.
		tempArr = getRandomNumbers(start, end, maximum * maximum / 2 );
		bitmapsDrawables = new BitmapDrawable[ maximum * maximum / 2 ];

        for(int i=0 ;i< tempArr.length;i++){
            Log.e("Arr", "tempArr["+i+"]"+tempArr[i]);
            Log.e("Arr",ResourcesCompat.getDrawable(getResources(),tempArr[i],null)+"");
        }

		// 랜덤한 이미지를 로드한다.

		for (int i = 0; i < bitmapsDrawables.length; i++) {
			bitmapsDrawables[ i ] = (BitmapDrawable) ResourcesCompat.getDrawable(getResources(),tempArr[i],null);
		}

		// 비트맵을 얻는다.
		bitmaps = new Bitmap[ maximum * maximum / 2 ];
		for (int i = 0; i < bitmaps.length; i++) {
			bitmaps[ i ] = bitmapsDrawables[i].getBitmap();
		}

	}

	private void recoverAllCards(){
		// 카드를 모두 뒤집힌 상태로 놓는다.
		for (int i = 0; i < buttons.length; i++){
			buttons[ i ].setImageBitmap( bitmapBack );
		}
	}

	private void recoverCard(){

		// 뒤집어본 카드를 다시 돌려놓는다.		
		buttons[ selectedIndex[ 0 ] ].setImageBitmap( bitmapBack );
		buttons[ selectedIndex[ 1 ] ].setImageBitmap( bitmapBack );

	}

	private void setViews(){
		int temp;

		temp = 0;

		mainLinear.removeAllViews();

		// 버튼 생성
		buttons = new ImageButton[maximum * maximum];
		for (int i = 0; i < buttons.length; i++)
			buttons[ i ] = new ImageButton(this);

		// 행을 생성한다 수평 리니어이다.
		linearRows = new LinearLayout[maximum];

		for (int i = 0; i < linearRows.length; i++) {

			linearRows[i] = new LinearLayout(this);
			linearRows[i].setGravity( Gravity.CENTER_HORIZONTAL ); // 가운데 정렬
			linearRows[i].setOrientation( LinearLayout.HORIZONTAL );

			for (int j = 0; j < maximum; j++) {
				// linearLayout 에 이미지버튼을 배치한다
				linearRows[i].addView(buttons[temp++]);
			}
			// 메인 LinearLayout 에 뷰를 배치한다,.
			mainLinear.addView(linearRows[i]);
		}
	}

	private void init(){

		is_start=false;
		time.setText(R.string.init_time);

		stBtn.setOnClickListener(this);
		stBtn.setVisibility(View.VISIBLE);

		// 이벤트 리스너 등록
        for (ImageButton button : buttons) {
            button.setOnClickListener(this);
            button.setVisibility(View.VISIBLE);
        }


		// selectedindex 는 사용자가 선택한 카드의 index 가 들어있다.
		// 이미 선택된 카드를 다시 선택했는지 판단한다.
		selectedIndex =  new int[ 2 ];
		for( int i = 0 ; i < selectedIndex.length ; i++ ){
			selectedIndex[ i ] = -1;
		}

		// correctindexarray 는 이미 맞춘 카드의 index 가 저장된다.
		correctIndexArray = new int[ maximum * maximum ];
		for( int i = 0 ; i < correctIndexArray.length ; i++ ){
			correctIndexArray[ i ] = -1;
		}

		// 선택한 카드의 이미지 index 가 저장된다.
		// 이미지가 서로 일치하는지 판단한다,.
		selectedCardIndex = -1;

		// bPassible 이 true 일때만 버튼클릭이 작동한다.
		// 이것은 1 초동안 딜레이상태일때 다른 버튼이 클릭되는것을 막는다.
		bPassible = true;

	}

	public void onClick(View v) {

		if(!is_start&&(v.equals(buttons[0])||v.equals(buttons[1])||v.equals(buttons[2])||v.equals(buttons[3])||v.equals(buttons[4])
				||v.equals(buttons[5]) ||v.equals(buttons[6])||v.equals(buttons[7])||v.equals(buttons[8])||v.equals(buttons[9])
				||v.equals(buttons[10])||v.equals(buttons[11])||v.equals(buttons[12])||v.equals(buttons[13])||v.equals(buttons[14])
				||v.equals(buttons[15]))) {

            is_start = !is_start;
            new Thread(this).start();
            sw.start();

        }
		if(!bPassible)
			return ;

		for( int i = 0 ; i < buttons.length ; i++ ){
			if( v.equals( buttons[i] ) ){

				// 이미 선택한 카드일경우
				if( selectedIndex[ 0 ] == i ){
					tv.setTextColor( Color.RED );
					tv.setText( R.string.already_selected );
					return ;
				}

				// 이미 정답을 맞춘 카드일 경우
				if( correctIndexArray[ i ] != -1 ){
					tv.setTextColor( Color.RED );
					tv.setText( R.string.already_correct );
					return ;
				}

				// 클릭한 이미지를 보여준다.
				buttons[ i ].setImageBitmap( bitmaps[ indexes[ i ] ] );

				// 처음클릭한 이미지이면
				if( selectedIndex[ 0 ] == -1 ){
					selectedCardIndex = indexes[ i ];
					tv.setTextColor( Color.YELLOW );
					tv.setText( R.string.choice2 ); // 두번째를 클릭해주세요
					selectedIndex[ 0 ] = i;
				}
				else{
					//두번째 클릭한 이미지이면
					selectedIndex[ 1 ] = i;

					// 이미지가 일치하면
					if( selectedCardIndex == indexes[ i ] ){

						tv.setTextColor( Color.BLUE );
						tv.setText( R.string.correct ); // 맞췄습니다!
						// 맞춘이미지 index 를 저장한다.
						correctIndexArray[ selectedIndex[ 0 ] ] = 1;
						correctIndexArray[ selectedIndex[ 1 ] ] = 1;
						for (int aCorrectIndexArray : correctIndexArray) {

							// 맞추지 못한 카드가 있을 경우
							if (aCorrectIndexArray == -1) {
								// 1초후에 핸들러 실행
								Handler.sendEmptyMessageDelayed(1, 50);
								return;
							}
						}

						is_start=false;
						sw.stop();

						tv.setTextColor( Color.BLUE );
						tv.setText( R.string.clear );
						Toast.makeText(getApplicationContext()," : "+strTime, Toast.LENGTH_LONG).show();


						bPassible = false;

						return ;
					}
					else{
						tv.setTextColor( Color.RED );
						tv.setText( R.string.wrong ); // 틀렸습니다.
						Handler.sendEmptyMessageDelayed( 0 , 500);
						bPassible = false;
					}
				}
				break;
			}
		}
	}

	public void run(){
		while(is_start){
			Handler.sendEmptyMessage(2);
			try{
				Thread.sleep(50);
			}catch(Exception ig){
				ig.printStackTrace();
			}
		}
	}

	Handler Handler = new Handler(){
		@Override
		public void handleMessage( Message msg ){

			switch( msg.what )
			{
				case 0:	// 틀린경우
					recoverCard();
				case 1: // 맞은경우
					selectedIndex[ 0 ] = -1;
					selectedIndex[ 1 ] = -1;
					bPassible = true;
					tv.setTextColor( Color.BLUE );
					tv.setText( R.string.choice1 );
					break;
				case 2:
					strTime=String.format("%.02f",sw.getFormatF());

					int nLen=strTime.length();

					if(nLen!=6)
						while(6-nLen++>0)
							strTime="0"+strTime;

					time.setText(strTime);
					super.handleMessage(msg);

			}
		}
	};

	public int[] getRandomNumbers(int start, int end, int num) {
		// 이 메서드는 start ~ end 범위의 정수중 num 갯수만큼의 정수를
		// 랜덤하게 중복없이 뽑아냅니다.
		int size; // 크기입니다
		int temp; // 임시변수입니다.

		if (start > end || start == end) // 시작위치가 끝 위치보다 크거나 같으면 실패입니다.
			return null;

		size = end - start + 1; // size 의 크기를 구합니다.

		if (size < num) // size 의 크기보다 원하는 숫자의 갯수가 많으면 실패입니다.
			return null;

		int[] numbers;
		double[] dRandom;
		int[] result = null;

		try {

			numbers = new int[size];
			dRandom = new double[size];
			result = new int[num];

			// 랜덤한 수를 size 만큼 생성합니다.
			for (int i = 0; i < dRandom.length; i++) {
				dRandom[i] = Math.random();
			}
			// 차대로 할당 후
			temp = start;
			for (int i = 0; i < numbers.length; i++) {
				numbers[i] = temp++;
			}

			// 랜덤으로 생성된 숫자를 정렬하여 섞습니다.
			for (int i = 0; i < dRandom.length; i++) {
				for (int j = 0; j < dRandom.length - 1; j++) {
					double tmp = dRandom[j];
					temp = numbers[j];

					if (dRandom[j] > dRandom[j + 1]) {
						dRandom[j] = dRandom[j + 1];
						dRandom[j + 1] = tmp;
						numbers[j] = numbers[j + 1];
						numbers[j + 1] = temp;
					}
				}
			}
            // 결과를 리턴합니다.
			System.arraycopy(numbers, 0, result, 0, result.length);

		} catch (Exception e) {

        }

		return result;
	}

}