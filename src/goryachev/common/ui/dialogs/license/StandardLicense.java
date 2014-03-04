// Copyright (c) 2011-2014 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.dialogs.license;
import goryachev.common.ui.Application;
import goryachev.common.ui.ApplicationLicense;
import goryachev.common.ui.text.CDocumentBuilder;
import goryachev.common.util.TXT;
import javax.swing.text.StyledDocument;


public class StandardLicense
	implements ApplicationLicense
{
	private final String author = "Andy Goryachev";
	private final String supportEmail = "support@goryachev.com";
	private boolean military = true;
	private int num;
	private int section;
	private CDocumentBuilder b;
	
	
	public StandardLicense()
	{
	}
	
	
	public String getTitle()
	{
		return TXT.get("StandardLicense.title", "End-User License Agreement");
	}
	
	
	public void setMilitaryClause(boolean on)
	{
		military = on;
	}
	
	
	protected void sp()
	{
		b.append("  ");
	}
	
	
	protected void nl()
	{
		b.append("\n");
	}
	
	
	protected void a(String text)
	{
		b.append(text);
	}
	
	
	protected void addSection(String name)
	{
		name = (name == null ? null : section + ". " + name);
		
		b.nl();
		b.nl();

		if(name != null)
		{
			b.append(name);
			b.append("  ");
		}

		section++;
	}
	
	
	public StyledDocument getDocument()
	{
		section = 1;
		
		b = new CDocumentBuilder();
		b.setCenter(true);
		b.setBold(true);
		b.append("END-USER LICENSE AGREEMENT");
		b.setBold(false);
		b.nl().nl();
		b.setCenter(false);
				
		a("IMPORTANT-READ CAREFULLY: This End-User License Agreement (\"EULA\") is a legal agreement between you (either an individual or a single entity) and ");
		a(author);
		a(" for the ");
		a(Application.getTitle());
		a(" software product, which includes the User Manual, any associated SOFTWARE components, any media, any printed materials other than the User Manual, and any \"online\" or electronic documentation (\"SOFTWARE\").  ");
		a("By installing, copying, or otherwise using the SOFTWARE, you agree to be bound by the terms of this EULA.  ");
		a("If you do not agree to the terms of this EULA, do not install or use the SOFTWARE.");
		
		addSection("The SOFTWARE is licensed, not sold.");
		
		addSection("GRANT OF LICENSE.");
		nl();
		nl();
		a("(a) Evaluation Configuration.  You may use the evaluation configuration of SOFTWARE without charge for a period of 30 days.  ");
		a("At the end of the evaluation period, you should decide whether you want to keep the Software.  ");
		a("You must purchase a Registration Key if you want to continue using the Software after the evaluation period ends.  ");
		a("If you decide for any reason that you do not want to purchase a Registration Key, you must stop using the Software and remove it from your computer.");
		nl();
		nl();
		a("(b) Registered Configuration.  ");
		a("After you have purchased the license for SOFTWARE, and have received the Registration Key enabling the registered copy, you are licensed to install the SOFTWARE only on the number of computers corresponding to the number of licenses purchased.");
		
		addSection("RESTRICTIONS.");
		a("You may not reverse engineer, de-compile, or disassemble the SOFTWARE, except and only to the extent that such activity is expressly permitted by applicable law notwithstanding this limitation.");
		if(military)
		{
			sp();
			b.setBold(true);
			a("You may not use the SOFTWARE in a military domain; in the design, construction, operation or maintenance of weapons.  ");
			b.setBold(false);
			sp();
		}
		a("You may not rent, lease, or lend the SOFTWARE.  You may permanently transfer all of your rights under this EULA, provided the recipient agrees to the terms of this EULA."); 
		
		addSection("SUPPORT SERVICES.");
		a(author);
		a(" may provide you with support services related to the SOFTWARE.  ");
		a("Use of Support Services is governed by the policy described in the user manual, in online documentation, and/or other provided materials, as they may be modified from time to time.  ");
		a("Any supplemental SOFTWARE code provided to you as part of the Support Services shall be considered part of the SOFTWARE and subject to the terms and conditions of this EULA.");
		
		addSection("TERMINATION.");
		a("Without prejudice to any other rights, ");
		a(author);
		a(" may terminate this EULA if you fail to comply with the terms and conditions of this EULA.  ");
		a("In such event, you must destroy all copies of the SOFTWARE.");
		
		addSection("COPYRIGHT.");
		a("The SOFTWARE is protected by United States copyright law and international treaty provisions.  ");
		a("You acknowledge that no title to the intellectual property in the SOFTWARE is transferred to you.  ");
		a("You further acknowledge that title and full ownership rights to the SOFTWARE will remain the exclusive property of ");
		a(author);
		a(" and you will not acquire any rights to the SOFTWARE except as expressly set forth in this license.  ");
		a("You agree that any copies of the SOFTWARE will contain the same proprietary notices which appear on and in the SOFTWARE.");
		
		addSection("EXPORT RESTRICTIONS.");
		a("You agree that you will not export or re-export the SOFTWARE to any country, person, entity, or end user subject to U.S.A. export restrictions.  ");
		a("Restricted countries currently include, but are not necessarily limited to Cuba, Iran, Iraq, Libya, North Korea, Sudan, and Syria.  ");
		a("You warrant and represent that neither the U.S.A. Bureau of Export Administration nor any other federal agency has suspended, revoked or denied your export privileges.");
		
		addSection("LIMITED WARRANTY.");
		a(author);
		a(" warrants that the Software will perform substantially in accordance with the accompanying written materials for a period of 90 days from the date of your receipt of the Software.  ");
		a("Any implied warranties on the Software are limited to 90 days.  ");
		a("Some states do not allow limitations on duration of an implied warranty, so the above limitation may not apply to you.  ");
		a(author.toUpperCase());
		a(" DISCLAIMS ALL OTHER WARRANTIES, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, AND NON-INFRINGEMENT, WITH RESPECT TO THE SOFTWARE AND THE ACCOMPANYING WRITTEN MATERIALS.  ");
		a("This limited warranty gives you specific legal rights.  ");
		a("You may have others, which vary from state to state.");
		
		addSection("LIMITATION OF LIABILITY.");
		a("IN NO EVENT SHALL ");
		a(author.toUpperCase());
		a(" OR ITS SUPPLIERS BE LIABLE TO YOU FOR ANY CONSEQUENTIAL, SPECIAL, INCIDENTAL, OR INDIRECT DAMAGES OF ANY KIND ARISING OUT OF THE DELIVERY, PERFORMANCE, OR USE OF THE SOFTWARE, EVEN IF ");
		a(author.toUpperCase());
		a(" HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.  ");
		a("IN ANY EVENT, ");
		a(author.toUpperCase());
		a("'S LIABILITY FOR ANY CLAIM, WHETHER IN CONTRACT, TORT, OR ANY OTHER THEORY OF LIABILITY WILL NOT EXCEED THE LICENSE FEE PAID BY YOU.");
		
		addSection("MISCELLANEOUS.");
		a("If you acquired the SOFTWARE in the United States, this EULA is governed by the laws of the state of California.  If you acquired the SOFTWARE outside of the United States, then local laws may apply.");
		
		addSection(null);

		a("If you have any questions concerning this EULA, please send electronic mail to: ");
		a(supportEmail);
		a("\n");
		
		return b.getDocument();
	}
}
