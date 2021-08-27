package com.russvkm.askacharya.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Html
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import com.russvkm.askacharya.R
import com.russvkm.askacharya.activity.MainActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_article_detail.view.*


class ArticleDetail : Fragment() {

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val args=ArticleDetailArgs.fromBundle(requireArguments())
        val bundle=args.articleDetailArgs
        val actionBar=(activity as MainActivity).supportActionBar
        actionBar!!.setDisplayShowTitleEnabled(true)
        actionBar.title=bundle.articleTitle
        val root=inflater.inflate(R.layout.fragment_article_detail, container, false)
        root.fullArticleView.text=HtmlCompat.fromHtml("<html><body><h1><font color='golden'>${bundle.articleTitle} :-</font></h1><br>${bundle.article}<body></html>",HtmlCompat.FROM_HTML_MODE_LEGACY)
        if (bundle.articleImage.isNotEmpty()){
            Picasso.get()
                .load(bundle.articleImage)
                .into(root.articleImageView)
        }else{
            root.articleImageView.setImageResource(R.drawable.person_black)
        }
        return root
    }
}