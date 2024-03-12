import {useState, useEffect} from "react";
import {useNavigate} from "react-router-dom";
import clsx from "clsx";
import {toast} from "react-toastify";
import validator from "validator";
import {signup} from "../../api/api";

const Signup = () => {
    const navigate = useNavigate();

    const [loadingView, setLoadingView] = useState(false);
    const [name, setName] = useState("");
    const [workEmail, setWorkEmail] = useState("");
    const [country, setCountry] = useState("");
    const [receiveUpdates, setReceiveUpdates] = useState(false);
    const prefersDarkMode = window.matchMedia(
        "(prefers-color-scheme: dark)"
    ).matches;
    const [isDarkMode, setIsDarkMode] = useState(
        localStorage.getItem("theme")
            ? localStorage.getItem("theme") === "dark"
            : prefersDarkMode
                ? true
                : false
    );
    const [meter, setMeter] = useState(false);
    const [password, setPassword] = useState("");

    const atLeastOneUppercase = /[A-Z]/g; // capital letters from A to Z
    const atLeastOneLowercase = /[a-z]/g; // small letters from a to z
    const atLeastOneNumeric = /[0-9]/g; // numbers from 0 to 9
    const atLeastOneSpecialChar = /[#?!@$%^&*-]/g; // any of the special characters within the square brackets
    const eightCharsOrMore = /.{8,}/g; // eight characters or more

    const passwordTracker = {
        uppercase: password.match(atLeastOneUppercase),
        lowercase: password.match(atLeastOneLowercase),
        number: password.match(atLeastOneNumeric),
        specialChar: password.match(atLeastOneSpecialChar),
        eightCharsOrGreater: password.match(eightCharsOrMore),
    };

    useEffect(() => {
        if (isDarkMode) {
            document.documentElement.classList.add("dark");
        } else {
            document.documentElement.classList.remove("dark");
        }
    }, [isDarkMode]);

    const handleCreateAccount = async () => {
        if (!validator.isEmail(workEmail)) {
            toast.error("Please enter a valid email!");
            return;
        }

        setLoadingView(true);

        try {
            await signup(name, workEmail, password, country, receiveUpdates);
            setLoadingView(false);
            navigate("/signin");
        } catch (err) {
            setLoadingView(false);
            console.log(err);
            toast.error(err.response.data);
        }
    };

    return (
        <div className="w-full min-h-screen flex flex-row">
            <div className="flex flex-col justify-center gap-16 w-full">
                <div className="relative flex flex-col bg-main justify-center w-full">
                    <div className="flex flex-col justify-center gap-8 sm:w-1/3 mx-auto px-4">
                        <h2 className="section-heading text-xl sm:text-[32px] text-center font-bold text-primary-500">
                            Sign up
                        </h2>
                        <div className="flex flex-col gap-6 text-sm text-[#111827] dark:text-white">
                            <div className="flex flex-col gap-[10px]">
                                <p>
                                    Name <span className="text-[#E03137]">*</span>
                                </p>
                                <input
                                    id="name"
                                    type="text"
                                    className="border border-input rounded-[10px] block w-full px-5 py-4 focus:ring-0 focus:border-input dark:text-black"
                                    value={name}
                                    onChange={(e) => setName(e.target.value)}
                                    required
                                />
                            </div>
                            <div className="flex flex-col gap-[10px]">
                                <p>
                                    E-Mail <span className="text-[#E03137]">*</span>
                                </p>
                                <input
                                    id="email"
                                    type="email"
                                    className="border border-input rounded-[10px] block w-full px-5 py-4 focus:ring-0 focus:border-input dark:text-black"
                                    value={workEmail}
                                    onChange={(e) => setWorkEmail(e.target.value)}
                                    required
                                />
                            </div>
                            <div className="flex flex-col gap-[10px]">
                                <p>
                                    Password <span className="text-[#E03137]">*</span>
                                </p>
                                <input
                                    id="password"
                                    type="password"
                                    className="border border-input rounded-[10px] block w-full px-5 py-4 focus:ring-0 focus:border-input dark:text-black"
                                    value={password}
                                    onFocus={() => setMeter(true)}
                                    onChange={(e) => setPassword(e.target.value)}
                                    required
                                />
                                {meter && (
                                    <div>
                                        <div className="password-strength-meter"></div>
                                        <div className="flex flex-col gap-2">
                                            <div className="flex gap-2 items-center ml-3">
                                                <div
                                                    className={clsx(
                                                        "rounded-full p-1 fill-current",
                                                        passwordTracker.eightCharsOrGreater
                                                            ? "bg-green-200 text-green-700"
                                                            : "bg-red-200 text-red-700"
                                                    )}
                                                >
                                                    <svg
                                                        className="w-4 h-4"
                                                        fill="none"
                                                        viewBox="0 0 24 24"
                                                        stroke="currentColor"
                                                    >
                                                        {passwordTracker.eightCharsOrGreater ? (
                                                            <path
                                                                strokeLinecap="round"
                                                                strokeLinejoin="round"
                                                                strokeWidth="2"
                                                                d="M5 13l4 4L19 7"
                                                            />
                                                        ) : (
                                                            <path
                                                                strokeLinecap="round"
                                                                strokeLinejoin="round"
                                                                strokeWidth="2"
                                                                d="M6 18L18 6M6 6l12 12"
                                                            />
                                                        )}
                                                    </svg>
                                                </div>
                                                <span
                                                    className={clsx(
                                                        "text-sm font-medium",
                                                        passwordTracker.eightCharsOrGreater
                                                            ? "text-green-700"
                                                            : "text-red-700"
                                                    )}
                                                >
                          At least 8 characters required
                        </span>
                                            </div>
                                            <div className="flex gap-2 items-center ml-3">
                                                <div
                                                    className={clsx(
                                                        "rounded-full p-1 fill-current",
                                                        passwordTracker.uppercase
                                                            ? "bg-green-200 text-green-700"
                                                            : "bg-red-200 text-red-700"
                                                    )}
                                                >
                                                    <svg
                                                        className="w-4 h-4"
                                                        fill="none"
                                                        viewBox="0 0 24 24"
                                                        stroke="currentColor"
                                                    >
                                                        {passwordTracker.uppercase ? (
                                                            <path
                                                                strokeLinecap="round"
                                                                strokeLinejoin="round"
                                                                strokeWidth="2"
                                                                d="M5 13l4 4L19 7"
                                                            />
                                                        ) : (
                                                            <path
                                                                strokeLinecap="round"
                                                                strokeLinejoin="round"
                                                                strokeWidth="2"
                                                                d="M6 18L18 6M6 6l12 12"
                                                            />
                                                        )}
                                                    </svg>
                                                </div>
                                                <span
                                                    className={clsx(
                                                        "text-sm font-medium",
                                                        passwordTracker.uppercase
                                                            ? "text-green-700"
                                                            : "text-red-700"
                                                    )}
                                                >
                          Password should contain with one uppercase
                        </span>
                                            </div>
                                            <div className="flex gap-2 items-center ml-3">
                                                <div
                                                    className={clsx(
                                                        "rounded-full p-1 fill-current",
                                                        passwordTracker.lowercase
                                                            ? "bg-green-200 text-green-700"
                                                            : "bg-red-200 text-red-700"
                                                    )}
                                                >
                                                    <svg
                                                        className="w-4 h-4"
                                                        fill="none"
                                                        viewBox="0 0 24 24"
                                                        stroke="currentColor"
                                                    >
                                                        {passwordTracker.lowercase ? (
                                                            <path
                                                                strokeLinecap="round"
                                                                strokeLinejoin="round"
                                                                strokeWidth="2"
                                                                d="M5 13l4 4L19 7"
                                                            />
                                                        ) : (
                                                            <path
                                                                strokeLinecap="round"
                                                                strokeLinejoin="round"
                                                                strokeWidth="2"
                                                                d="M6 18L18 6M6 6l12 12"
                                                            />
                                                        )}
                                                    </svg>
                                                </div>
                                                <span
                                                    className={clsx(
                                                        "text-sm font-medium",
                                                        passwordTracker.lowercase
                                                            ? "text-green-700"
                                                            : "text-red-700"
                                                    )}
                                                >
                          Password should contain with one lowercase
                        </span>
                                            </div>
                                            <div className="flex gap-2 items-center ml-3">
                                                <div
                                                    className={clsx(
                                                        "rounded-full p-1 fill-current",
                                                        passwordTracker.specialChar
                                                            ? "bg-green-200 text-green-700"
                                                            : "bg-red-200 text-red-700"
                                                    )}
                                                >
                                                    <svg
                                                        className="w-4 h-4"
                                                        fill="none"
                                                        viewBox="0 0 24 24"
                                                        stroke="currentColor"
                                                    >
                                                        {passwordTracker.specialChar ? (
                                                            <path
                                                                strokeLinecap="round"
                                                                strokeLinejoin="round"
                                                                strokeWidth="2"
                                                                d="M5 13l4 4L19 7"
                                                            />
                                                        ) : (
                                                            <path
                                                                strokeLinecap="round"
                                                                strokeLinejoin="round"
                                                                strokeWidth="2"
                                                                d="M6 18L18 6M6 6l12 12"
                                                            />
                                                        )}
                                                    </svg>
                                                </div>
                                                <span
                                                    className={clsx(
                                                        "text-sm font-medium",
                                                        passwordTracker.specialChar
                                                            ? "text-green-700"
                                                            : "text-red-700"
                                                    )}
                                                >
                          Password should contain with one special character
                        </span>
                                            </div>
                                            <div className="flex gap-2 items-center ml-3">
                                                <div
                                                    className={clsx(
                                                        "rounded-full p-1 fill-current",
                                                        passwordTracker.number
                                                            ? "bg-green-200 text-green-700"
                                                            : "bg-red-200 text-red-700"
                                                    )}
                                                >
                                                    <svg
                                                        className="w-4 h-4"
                                                        fill="none"
                                                        viewBox="0 0 24 24"
                                                        stroke="currentColor"
                                                    >
                                                        {passwordTracker.number ? (
                                                            <path
                                                                strokeLinecap="round"
                                                                strokeLinejoin="round"
                                                                strokeWidth="2"
                                                                d="M5 13l4 4L19 7"
                                                            />
                                                        ) : (
                                                            <path
                                                                strokeLinecap="round"
                                                                strokeLinejoin="round"
                                                                strokeWidth="2"
                                                                d="M6 18L18 6M6 6l12 12"
                                                            />
                                                        )}
                                                    </svg>
                                                </div>
                                                <span
                                                    className={clsx(
                                                        "text-sm font-medium",
                                                        passwordTracker.number
                                                            ? "text-green-700"
                                                            : "text-red-700"
                                                    )}
                                                >
                          Password should contain with one number
                        </span>
                                            </div>
                                        </div>
                                    </div>
                                )}
                            </div>
                            <div className="flex flex-col gap-[10px]">
                                <p>
                                    Country <span className="text-[#E03137]">*</span>
                                </p>
                                <select
                                    id="country"
                                    className="border border-input rounded-[10px] block w-full px-5 py-4 focus:ring-0 focus:border-input dark:text-black"
                                    value={country}
                                    onChange={(e) => setCountry(e.target.value)}
                                    required
                                >
                                    <option value="">Select a country</option>
                                    <option value="US">United States</option>
                                    <option value="GB">United Kingdom</option>
                                    <option value="AU">Australia</option>
                                    <option disabled>──────────</option>
                                    <option value="AF">Afghanistan</option>
                                    <option value="AL">Albania</option>
                                    <option value="DZ">Algeria</option>
                                    <option value="AS">American Samoa</option>
                                    <option value="AD">Andorra</option>
                                    <option value="AO">Angola</option>
                                    <option value="AI">Anguilla</option>
                                    <option value="AQ">Antarctica</option>
                                    <option value="AG">Antigua & Barbuda</option>
                                    <option value="AR">Argentina</option>
                                    <option value="AM">Armenia</option>
                                    <option value="AW">Aruba</option>
                                    <option value="AU">Australia</option>
                                    <option value="AT">Austria</option>
                                    <option value="AZ">Azerbaijan</option>
                                    <option value="BS">Bahamas</option>
                                    <option value="BH">Bahrain</option>
                                    <option value="BD">Bangladesh</option>
                                    <option value="BB">Barbados</option>
                                    <option value="BY">Belarus</option>
                                    <option value="BE">Belgium</option>
                                    <option value="BZ">Belize</option>
                                    <option value="BJ">Benin</option>
                                    <option value="BM">Bermuda</option>
                                    <option value="BT">Bhutan</option>
                                    <option value="BO">Bolivia</option>
                                    <option value="BA">Bosnia & Herzegovina</option>
                                    <option value="BW">Botswana</option>
                                    <option value="BV">Bouvet Island</option>
                                    <option value="BR">Brazil</option>
                                    <option value="IO">British Indian Ocean Territory</option>
                                    <option value="VG">British Virgin Islands</option>
                                    <option value="BN">Brunei</option>
                                    <option value="BG">Bulgaria</option>
                                    <option value="BF">Burkina Faso</option>
                                    <option value="BI">Burundi</option>
                                    <option value="KH">Cambodia</option>
                                    <option value="CM">Cameroon</option>
                                    <option value="CA">Canada</option>
                                    <option value="CV">Cape Verde</option>
                                    <option value="BQ">Caribbean Netherlands</option>
                                    <option value="KY">Cayman Islands</option>
                                    <option value="CF">Central African Republic</option>
                                    <option value="TD">Chad</option>
                                    <option value="CL">Chile</option>
                                    <option value="CN">China</option>
                                    <option value="CX">Christmas Island</option>
                                    <option value="CC">Cocos (Keeling) Islands</option>
                                    <option value="CO">Colombia</option>
                                    <option value="KM">Comoros</option>
                                    <option value="CG">Congo - Brazzaville</option>
                                    <option value="CD">Congo - Kinshasa</option>
                                    <option value="CK">Cook Islands</option>
                                    <option value="CR">Costa Rica</option>
                                    <option value="HR">Croatia</option>
                                    <option value="CU">Cuba</option>
                                    <option value="CW">Curaçao</option>
                                    <option value="CY">Cyprus</option>
                                    <option value="CZ">Czechia</option>
                                    <option value="CI">Côte d’Ivoire</option>
                                    <option value="DK">Denmark</option>
                                    <option value="DJ">Djibouti</option>
                                    <option value="DM">Dominica</option>
                                    <option value="DO">Dominican Republic</option>
                                    <option value="EC">Ecuador</option>
                                    <option value="EG">Egypt</option>
                                    <option value="SV">El Salvador</option>
                                    <option value="GQ">Equatorial Guinea</option>
                                    <option value="ER">Eritrea</option>
                                    <option value="EE">Estonia</option>
                                    <option value="SZ">Eswatini</option>
                                    <option value="ET">Ethiopia</option>
                                    <option value="FK">Falkland Islands</option>
                                    <option value="FO">Faroe Islands</option>
                                    <option value="FJ">Fiji</option>
                                    <option value="FI">Finland</option>
                                    <option value="FR">France</option>
                                    <option value="GF">French Guiana</option>
                                    <option value="PF">French Polynesia</option>
                                    <option value="TF">French Southern Territories</option>
                                    <option value="GA">Gabon</option>
                                    <option value="GM">Gambia</option>
                                    <option value="GE">Georgia</option>
                                    <option value="DE">Germany</option>
                                    <option value="GH">Ghana</option>
                                    <option value="GI">Gibraltar</option>
                                    <option value="GR">Greece</option>
                                    <option value="GL">Greenland</option>
                                    <option value="GD">Grenada</option>
                                    <option value="GP">Guadeloupe</option>
                                    <option value="GU">Guam</option>
                                    <option value="GT">Guatemala</option>
                                    <option value="GG">Guernsey</option>
                                    <option value="GN">Guinea</option>
                                    <option value="GW">Guinea-Bissau</option>
                                    <option value="GY">Guyana</option>
                                    <option value="HT">Haiti</option>
                                    <option value="HM">Heard & McDonald Islands</option>
                                    <option value="HN">Honduras</option>
                                    <option value="HK">Hong Kong SAR China</option>
                                    <option value="HU">Hungary</option>
                                    <option value="IS">Iceland</option>
                                    <option value="IN">India</option>
                                    <option value="ID">Indonesia</option>
                                    <option value="IR">Iran</option>
                                    <option value="IQ">Iraq</option>
                                    <option value="IE">Ireland</option>
                                    <option value="IM">Isle of Man</option>
                                    <option value="IL">Israel</option>
                                    <option value="IT">Italy</option>
                                    <option value="JM">Jamaica</option>
                                    <option value="JP">Japan</option>
                                    <option value="JE">Jersey</option>
                                    <option value="JO">Jordan</option>
                                    <option value="KZ">Kazakhstan</option>
                                    <option value="KE">Kenya</option>
                                    <option value="KI">Kiribati</option>
                                    <option value="KW">Kuwait</option>
                                    <option value="KG">Kyrgyzstan</option>
                                    <option value="LA">Laos</option>
                                    <option value="LV">Latvia</option>
                                    <option value="LB">Lebanon</option>
                                    <option value="LS">Lesotho</option>
                                    <option value="LR">Liberia</option>
                                    <option value="LY">Libya</option>
                                    <option value="LI">Liechtenstein</option>
                                    <option value="LT">Lithuania</option>
                                    <option value="LU">Luxembourg</option>
                                    <option value="MO">Macao SAR China</option>
                                    <option value="MG">Madagascar</option>
                                    <option value="MW">Malawi</option>
                                    <option value="MY">Malaysia</option>
                                    <option value="MV">Maldives</option>
                                    <option value="ML">Mali</option>
                                    <option value="MT">Malta</option>
                                    <option value="MH">Marshall Islands</option>
                                    <option value="MQ">Martinique</option>
                                    <option value="MR">Mauritania</option>
                                    <option value="MU">Mauritius</option>
                                    <option value="YT">Mayotte</option>
                                    <option value="MX">Mexico</option>
                                    <option value="FM">Micronesia</option>
                                    <option value="MD">Moldova</option>
                                    <option value="MC">Monaco</option>
                                    <option value="MN">Mongolia</option>
                                    <option value="ME">Montenegro</option>
                                    <option value="MS">Montserrat</option>
                                    <option value="MA">Morocco</option>
                                    <option value="MZ">Mozambique</option>
                                    <option value="MM">Myanmar (Burma)</option>
                                    <option value="NA">Namibia</option>
                                    <option value="NR">Nauru</option>
                                    <option value="NP">Nepal</option>
                                    <option value="NL">Netherlands</option>
                                    <option value="NC">New Caledonia</option>
                                    <option value="NZ">New Zealand</option>
                                    <option value="NI">Nicaragua</option>
                                    <option value="NE">Niger</option>
                                    <option value="NG">Nigeria</option>
                                    <option value="NU">Niue</option>
                                    <option value="NF">Norfolk Island</option>
                                    <option value="KP">North Korea</option>
                                    <option value="MK">North Macedonia</option>
                                    <option value="MP">Northern Mariana Islands</option>
                                    <option value="NO">Norway</option>
                                    <option value="OM">Oman</option>
                                    <option value="PK">Pakistan</option>
                                    <option value="PW">Palau</option>
                                    <option value="PS">Palestinian Territories</option>
                                    <option value="PA">Panama</option>
                                    <option value="PG">Papua New Guinea</option>
                                    <option value="PY">Paraguay</option>
                                    <option value="PE">Peru</option>
                                    <option value="PH">Philippines</option>
                                    <option value="PN">Pitcairn Islands</option>
                                    <option value="PL">Poland</option>
                                    <option value="PT">Portugal</option>
                                    <option value="PR">Puerto Rico</option>
                                    <option value="QA">Qatar</option>
                                    <option value="RO">Romania</option>
                                    <option value="RU">Russia</option>
                                    <option value="RW">Rwanda</option>
                                    <option value="RE">Réunion</option>
                                    <option value="WS">Samoa</option>
                                    <option value="SM">San Marino</option>
                                    <option value="SA">Saudi Arabia</option>
                                    <option value="SN">Senegal</option>
                                    <option value="RS">Serbia</option>
                                    <option value="SC">Seychelles</option>
                                    <option value="SL">Sierra Leone</option>
                                    <option value="SG">Singapore</option>
                                    <option value="SX">Sint Maarten</option>
                                    <option value="SK">Slovakia</option>
                                    <option value="SI">Slovenia</option>
                                    <option value="SB">Solomon Islands</option>
                                    <option value="SO">Somalia</option>
                                    <option value="ZA">South Africa</option>
                                    <option value="GS">South Georgia & South Sandwich Islands</option>
                                    <option value="KR">South Korea</option>
                                    <option value="SS">South Sudan</option>
                                    <option value="ES">Spain</option>
                                    <option value="LK">Sri Lanka</option>
                                    <option value="BL">St Barthélemy</option>
                                    <option value="SH">St Helena</option>
                                    <option value="KN">St Kitts & Nevis</option>
                                    <option value="LC">St Lucia</option>
                                    <option value="MF">St Martin</option>
                                    <option value="PM">St Pierre & Miquelon</option>
                                    <option value="VC">St Vincent & the Grenadines</option>
                                    <option value="SD">Sudan</option>
                                    <option value="SR">Suriname</option>
                                    <option value="SJ">Svalbard & Jan Mayen</option>
                                    <option value="SE">Sweden</option>
                                    <option value="CH">Switzerland</option>
                                    <option value="SY">Syria</option>
                                    <option value="ST">São Tomé & Príncipe</option>
                                    <option value="TW">Taiwan</option>
                                    <option value="TJ">Tajikistan</option>
                                    <option value="TZ">Tanzania</option>
                                    <option value="TH">Thailand</option>
                                    <option value="TL">Timor-Leste</option>
                                    <option value="TG">Togo</option>
                                    <option value="TK">Tokelau</option>
                                    <option value="TO">Tonga</option>
                                    <option value="TT">Trinidad & Tobago</option>
                                    <option value="TN">Tunisia</option>
                                    <option value="TR">Turkey</option>
                                    <option value="TM">Turkmenistan</option>
                                    <option value="TC">Turks & Caicos Islands</option>
                                    <option value="TV">Tuvalu</option>
                                    <option value="UM">US Outlying Islands</option>
                                    <option value="VI">US Virgin Islands</option>
                                    <option value="UG">Uganda</option>
                                    <option value="UA">Ukraine</option>
                                    <option value="AE">United Arab Emirates</option>
                                    <option value="GB">United Kingdom</option>
                                    <option value="US">United States</option>
                                    <option value="UY">Uruguay</option>
                                    <option value="UZ">Uzbekistan</option>
                                    <option value="VU">Vanuatu</option>
                                    <option value="VA">Vatican City</option>
                                    <option value="VE">Venezuela</option>
                                    <option value="VN">Vietnam</option>
                                    <option value="WF">Wallis & Futuna</option>
                                    <option value="EH">Western Sahara</option>
                                    <option value="YE">Yemen</option>
                                    <option value="ZM">Zambia</option>
                                    <option value="ZW">Zimbabwe</option>
                                    <option value="AX">Åland Islands</option>
                                </select>
                            </div>
                            <div className="flex flex-row items-center gap-2">
                                <input
                                    id="game-updates"
                                    type="checkbox"
                                    className="border border-input rounded text-primary-500 focus:ring-0 focus:border-input"
                                    checked={receiveUpdates}
                                    onChange={(e) => setReceiveUpdates(e.target.checked)}
                                />
                                <label htmlFor="game-updates" className="text-sm text-white dark:text-white">
                                    I want to receive news and updates
                                </label>
                            </div>
                        </div>
                        <div className="flex flex-col gap-4">
                            <button
                                id="create-account"
                                type="button"
                                className={clsx(
                                    "flex items-center justify-center text-white focus:ring-0 font-bold text-center rounded-[10px] text-base px-6 py-5",
                                    name === "" ||
                                    workEmail === "" ||
                                    country === "" ||
                                    password === "" ||
                                    !passwordTracker.eightCharsOrGreater ||
                                    !passwordTracker.uppercase ||
                                    !passwordTracker.lowercase ||
                                    !passwordTracker.specialChar ||
                                    !passwordTracker.number
                                        ? "cursor-not-allowed bg-gray-400"
                                        : "bg-primary-500"
                                )}
                                onClick={handleCreateAccount}
                                disabled={
                                    loadingView === true ||
                                    name === "" ||
                                    workEmail === "" ||
                                    password === "" ||
                                    !passwordTracker.eightCharsOrGreater ||
                                    !passwordTracker.uppercase ||
                                    !passwordTracker.lowercase ||
                                    !passwordTracker.specialChar ||
                                    !passwordTracker.number
                                        ? true
                                        : false
                                }
                            >
                                {loadingView === true && (
                                    <svg
                                        aria-hidden="true"
                                        role="status"
                                        className="inline w-4 h-4 me-3 text-white animate-spin"
                                        viewBox="0 0 100 101"
                                        fill="none"
                                        xmlns="http://www.w3.org/2000/svg"
                                    >
                                        <path
                                            d="M100 50.5908C100 78.2051 77.6142 100.591 50 100.591C22.3858 100.591 0 78.2051 0 50.5908C0 22.9766 22.3858 0.59082 50 0.59082C77.6142 0.59082 100 22.9766 100 50.5908ZM9.08144 50.5908C9.08144 73.1895 27.4013 91.5094 50 91.5094C72.5987 91.5094 90.9186 73.1895 90.9186 50.5908C90.9186 27.9921 72.5987 9.67226 50 9.67226C27.4013 9.67226 9.08144 27.9921 9.08144 50.5908Z"
                                            fill="#E5E7EB"
                                        />
                                        <path
                                            d="M93.9676 39.0409C96.393 38.4038 97.8624 35.9116 97.0079 33.5539C95.2932 28.8227 92.871 24.3692 89.8167 20.348C85.8452 15.1192 80.8826 10.7238 75.2124 7.41289C69.5422 4.10194 63.2754 1.94025 56.7698 1.05124C51.7666 0.367541 46.6976 0.446843 41.7345 1.27873C39.2613 1.69328 37.813 4.19778 38.4501 6.62326C39.0873 9.04874 41.5694 10.4717 44.0505 10.1071C47.8511 9.54855 51.7191 9.52689 55.5402 10.0491C60.8642 10.7766 65.9928 12.5457 70.6331 15.2552C75.2735 17.9648 79.3347 21.5619 82.5849 25.841C84.9175 28.9121 86.7997 32.2913 88.1811 35.8758C89.083 38.2158 91.5421 39.6781 93.9676 39.0409Z"
                                            fill="currentColor"
                                        />
                                    </svg>
                                )}
                                Create Account
                            </button>
                            <p className="text-sm text-[#A0AEC0]">
                                Already have an account?{" "}
                                <a
                                    className="text-sm text-[#3562D4]"
                                    href="\signin"
                                    rel="noreferrer"
                                >
                                    Login Here
                                </a>
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Signup;
