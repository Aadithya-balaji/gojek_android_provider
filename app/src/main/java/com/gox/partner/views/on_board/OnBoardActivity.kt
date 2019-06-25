package com.gox.partner.views.on_board

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.databinding.ViewDataBinding
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.gox.base.base.BaseActivity
import com.gox.partner.R
import com.gox.partner.databinding.ActivityOnBoardBinding
import com.gox.partner.views.sign_in.LoginActivity
import com.gox.partner.views.signup.RegistrationActivity

class OnBoardActivity : BaseActivity<ActivityOnBoardBinding>()
        , OnBoardNavigator {

    private lateinit var mBinding: ActivityOnBoardBinding
    private lateinit var viewPager: ViewPager

    private val onBoardItems = java.util.ArrayList<OnBoardItem>()
    private var mIndicator: CircleIndicator? = null

    override fun getLayoutId() = R.layout.activity_on_board

    override fun initView(mViewDataBinding: ViewDataBinding?) {

        this.mBinding = mViewDataBinding as ActivityOnBoardBinding
        val onBoardViewModel = OnBoardViewModel()
        onBoardViewModel.navigator = this

        mViewDataBinding.viewModel = onBoardViewModel
        mViewDataBinding.executePendingBindings()

        val list = ArrayList<WalkThrough>()
        list.add(WalkThrough(R.drawable.bg_walk_one,
                getString(R.string.walk_1), getString(R.string.walk_1_description)))
        list.add(WalkThrough(R.drawable.bg_walk_two,
                getString(R.string.walk_2), getString(R.string.walk_2_description)))
        list.add(WalkThrough(R.drawable.bg_walk_three,
                getString(R.string.walk_3), getString(R.string.walk_3_description)))
        viewPager = mViewDataBinding.viewpagerOnboard

        viewPager.adapter = MyViewPagerAdapter(this, list)
        viewPager.currentItem = 0
        mIndicator = findViewById(R.id.indicator)

        loadAdapter()

    }

    private fun loadAdapter() {

        val header = intArrayOf(R.string.walk_1, R.string.walk_2, R.string.walk_3)
        val desc = intArrayOf(R.string.walk_1_description,
                R.string.walk_2_description, R.string.walk_3_description)
        val imageId = intArrayOf(R.drawable.bg_walk_one, R.drawable.bg_walk_two, R.drawable.bg_walk_three)

        for (i in imageId.indices) {
            val item = OnBoardItem()
            item.imageID = imageId[i]
            item.title = (resources.getString(header[i]))
            item.description = (resources.getString(desc[i]))
            onBoardItems.add(item)
        }

        viewPager.adapter = IntroSliderAdapter(this, onBoardItems)
        mIndicator!!.setViewPager(viewPager)
        viewPager.currentItem = 0
    }

    override fun goToSignIn() = openActivity(LoginActivity::class.java, false)

    override fun goToSignUp() = openActivity(RegistrationActivity::class.java, false)
}

class MyViewPagerAdapter internal constructor(internal var context: Context, internal var list: List<WalkThrough>) : PagerAdapter() {

    override fun getCount() = list.size

    @NonNull
    override fun instantiateItem(@NonNull container: ViewGroup, position: Int): Any {
        val itemView = LayoutInflater.from(container.context).inflate(R.layout.pager_item, container, false)
        val walk = list[position]

        val title = itemView.findViewById<TextView>(R.id.title)
        val description = itemView.findViewById<TextView>(R.id.description)
        val imageView = itemView.findViewById<ImageView>(R.id.img_pager_item)

        title.text = walk.title
        description.text = walk.description
        Glide.with(context).load(walk.drawable).into(imageView)
        container.addView(itemView)

        return itemView
    }

    override fun isViewFromObject(@NonNull view: View, @NonNull obj: Any) = view === obj

    override fun destroyItem(@NonNull container: ViewGroup, position: Int, @NonNull `object`: Any) =
            container.removeView(`object` as View)
}