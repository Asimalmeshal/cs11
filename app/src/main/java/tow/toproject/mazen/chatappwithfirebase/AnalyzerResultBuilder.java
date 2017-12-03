package tow.toproject.mazen.chatappwithfirebase;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneCategory;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneScore;

import java.util.ArrayList;

/**
 * Class used to construct a UI to deliver information received from ToneAnalyzer to the user.
 */
class AnalyzerResultBuilder {


    private  ToneAnalysis analysis;
    private  Main2Activity context;

    String name;

    public AnalyzerResultBuilder (Main2Activity context, ToneAnalysis analysis) {
        this.context = context;
        this.analysis = analysis;
    }


    ArrayList MAZEN = new ArrayList();
    ArrayList ASSEM = new ArrayList();
    int i =0;
    public void buildAnalyzerResultView() {
        LinearLayout analysisLayout = new LinearLayout(context);
        analysisLayout.setOrientation(LinearLayout.VERTICAL);


        for (ToneCategory category : analysis.getDocumentTone().getTones()) {

            final ArrayList tones = (ArrayList) analysis.getDocumentTone().getTones();


            for (final ToneScore tone : category.getTones()) {


                ASSEM.add(tone.getName());

                double score = tone.getScore() ;//* (1 - 0.2) + 0.2;

                MAZEN.add((float)score);
             //   MAZEN.add((tone.getScore());



            }
         //   TextView toneTagView = (TextView)context.getLayoutInflater().inflate(R.layout.image_tag, null);

             int temp1 =0 ;
            float temp2= (float) MAZEN.get(0);
         for(int i=1 ; i<4 ;i++) {
             if (temp2 <= (float) MAZEN.get(i)) {
                 temp2 = (float) MAZEN.get(i);
                 temp1 = i;
             }
         }
             int finalTemp = temp1;
            String statues = "";
            TextView show = (TextView)context.findViewById(R.id.textToShow);

            if(ASSEM.get(finalTemp).equals("Joy") && (float)MAZEN.get(finalTemp) > 0.50){

                 statues = "Happy";
            }else if(ASSEM.get(finalTemp).equals("Joy") && (float)MAZEN.get(finalTemp) < 0.50){
                statues = "is normal";
            }else if(ASSEM.get(finalTemp).equals("Anger") && (float)MAZEN.get(finalTemp) > 0.50){
                statues = "Sad";

            }else if (ASSEM.get(finalTemp).equals("Anger") && (float)MAZEN.get(finalTemp) > 0.50){
                statues = "little sad";
            }


            show.setText(statues);
           // show.setText(ASSEM.get(finalTemp).toString()+"--->"+MAZEN.get(finalTemp));
           // show.setText(tempOne+" ---=>" +tempTow);
//            show.setText(ASSEM.get(finalTemp).toString()+"  --> "+ String.format(Locale.US, "%.2f", MAZEN.get(finalTemp) ) + "%");

//             toneTagView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    TextView t = (TextView)v;
//                    t.setText(ASSEM.get(finalTemp).toString()+"  : "+ String.format(Locale.US, "%.2f", MAZEN.get(finalTemp) ) + "%");
//                    System.out.println(tones);
//                }
//            });




        //    categoryTagContainer.addView(show);

            // Add the full tone_category view to the overall LinearLayout to be returned.
            break;
        }

       // return analysisLayout;
    }
}