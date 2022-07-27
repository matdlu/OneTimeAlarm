package pl.ascendit.onetimealarm.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import pl.ascendit.onetimealarm.R
import pl.ascendit.onetimealarm.databinding.FragmentAboutBinding


class AboutFragment : Fragment() {
    private lateinit var binding: FragmentAboutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentAboutBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mailSubject = getString(R.string.mail_subject)
        val mailAddress = getString(R.string.mail_address)
        val websiteUrl = getString(R.string.website_url)
        val githubUrl = getString(R.string.github_url)
        val linkedinUrl = getString(R.string.linkedin_url)

        binding.ivMail.setOnClickListener {
            sendMail(mailAddress, mailSubject)
        }

        binding.ivLinkedin.setOnClickListener {
            startBrowser(linkedinUrl)
        }

        binding.ivGithub.setOnClickListener {
            startBrowser(githubUrl)
        }

        binding.ivWebsite.setOnClickListener {
            startBrowser(websiteUrl)
        }
    }

    fun startBrowser(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    fun sendMail(email: String, subject: String) {
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.setData(Uri.parse("mailto:${email}"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        startActivity(Intent.createChooser(emailIntent, "Send email"))
    }
}
