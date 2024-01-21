import { format } from "date-fns";

import { HISTORY_TITLE } from "../../constants/strings";
import { BaseModal } from "./BaseModal";

export const HistoryModal = ({ isOpen, handleClose, historyData }) => {
  return (
    <BaseModal title={HISTORY_TITLE} isOpen={isOpen} handleClose={handleClose}>
      <div className="my-2 flex justify-center dark:text-white">
        <table>
          <thead>
            <tr>
              <th className="px-2">ID</th>
              <th className="px-2">TIME</th>
              <th className="px-2">GUESSES</th>
              <th className="px-2">OUTCOME</th>
            </tr>
          </thead>
          <tbody>
            {historyData.map((item, index) => {
              return (
                <tr key={index}>
                  <td className="px-2">{index + 1}</td>
                  <td className="px-2">{format(item.dateTimePlayed, 'd/M/yyyy')}</td>
                  <td className="px-2">{item.numberOfGuesses}</td>
                  <td className="px-2">{item.outcome == true ? "success" : "fail"}</td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>
    </BaseModal>
  );
};
