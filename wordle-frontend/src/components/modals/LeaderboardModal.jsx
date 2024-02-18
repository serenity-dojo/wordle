import {LEADERBOARD_TITLE} from "../../constants/strings";
import {BaseModal} from "./BaseModal";

export const LeaderboardModal = ({isOpen, handleClose, leaderboardData}) => {
    return (
        <BaseModal title={LEADERBOARD_TITLE} isOpen={isOpen} handleClose={handleClose}>
            <div className="my-2 flex justify-center dark:text-white">
                <table>
                    <thead>
                    <tr>
                        <th className="px-2">NAME</th>
                        <th className="px-2">COUNTRY</th>
                        <th className="px-2">GUESSES</th>
                        <th className="px-2">SUCCESS RATE</th>
                    </tr>
                    </thead>
                    <tbody>
                    {leaderboardData.map((item, index) => {
                        return (
                            <tr key={index}>
                                <td className="px-2">{item.name}</td>
                                <td className="px-2">{item.countryName}</td>
                                <td className="px-2">{item.totalTries}</td>
                                <td className="px-2">{item.successRate}%</td>
                            </tr>
                        );
                    })}
                    </tbody>
                </table>
            </div>
        </BaseModal>
    );
};
