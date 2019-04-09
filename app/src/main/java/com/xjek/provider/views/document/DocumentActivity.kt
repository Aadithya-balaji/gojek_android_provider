package com.xjek.provider.views.document

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import com.xjek.base.base.BaseActivity
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivityDocumentBinding
import com.xjek.provider.databinding.ActivityDocumentUploadBinding
import com.xjek.provider.views.uploaddocumentlist.VechileDetailActivity

class  DocumentActivity:BaseActivity<ActivityDocumentBinding>(),DocumentNavigator,
        View.OnClickListener{


    private  var activityDocumentUploadBinding:ActivityDocumentBinding?=null
    private  lateinit var  tvLeftHeader:TextView
    private  lateinit var ivBackArrow:ImageView
    private  lateinit var  tvRightHeader:TextView
    private  lateinit var  tbrivLogo:ImageView
    private  lateinit var   tbrIvRight:ImageView

    override fun getLayoutId(): Int {
        return R.layout.activity_document
    }

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        this.activityDocumentUploadBinding= mViewDataBinding as ActivityDocumentBinding
        val documentViewModel=DocumentViewModel()
        activityDocumentUploadBinding!!.documentviewmodel=documentViewModel

        tvLeftHeader=findViewById(R.id.tv_header)
        ivBackArrow=findViewById(R.id.iv_back)
        tvRightHeader=findViewById(R.id.tv_header)
        tbrivLogo=findViewById(R.id.tbr_iv_logo)
        tbrIvRight=findViewById(R.id.iv_right)


        tvLeftHeader.visibility= View.GONE
        ivBackArrow.visibility=View.VISIBLE
        tvRightHeader.visibility=View.VISIBLE
        tbrivLogo.visibility=View.GONE
        tbrIvRight.visibility=View.GONE

        tvRightHeader.setText(resources.getString(R.string.header_label_document_upload))
        ivBackArrow.setOnClickListener(this)

    }

    override fun uploadImages() {
        val intent= Intent(this@DocumentActivity,VechileDetailActivity::class.java)
        startActivity(intent)

    }

    override fun uploadPDF() {
        val intent= Intent(this@DocumentActivity,VechileDetailActivity::class.java)
        startActivity(intent)
    }

    override fun onClick(v: View?) {
        finish()
    }


}