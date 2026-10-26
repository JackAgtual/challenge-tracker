import { components } from "@/types/api";
import { Invite } from "./Invite";

type Props = {
  invites: components["schemas"]["ReceivedInviteResponse"][];
};

export function InviteList({ invites }: Props) {
  return (
    <div className="flex gap-4 flex-wrap justify-center sm:justify-start">
      {invites.map((invite) => (
        <Invite key={invite.id} invite={invite} />
      ))}
    </div>
  );
}
